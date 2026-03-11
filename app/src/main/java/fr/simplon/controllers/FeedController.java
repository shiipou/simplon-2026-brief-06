package fr.simplon.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.simplon.models.Post;
import fr.simplon.models.User;
import fr.simplon.utils.AuthUtils;
import fr.simplon.utils.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/feed")
public class FeedController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);
        String tab = req.getParameter("tab");
        if (tab == null || tab.isBlank()) {
            tab = "recommendations";
        }

        List<Post> feedPosts;

        if ("subscriptions".equals(tab)) {
            // Fil "Abonnements" : uniquement les posts des utilisateurs suivis
            List<User> followedUsers = DataStore.getFollowed(currentUser.getId());
            feedPosts = new ArrayList<>();
            for (Post post : DataStore.getPosts()) {
                if (!post.isComment() && !post.isDraft() && followedUsers.contains(post.getOwner())) {
                    feedPosts.add(post);
                }
            }
        } else {
            // Fil "Recommandations" : tous les posts (pas les commentaires)
            feedPosts = new ArrayList<>();
            for (Post post : DataStore.getPosts()) {
                if (!post.isComment() && !post.isDraft()) {
                    feedPosts.add(post);
                }
            }
        }

        // Tri par date de création décroissante
        feedPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        // Préparer les données supplémentaires pour chaque post
        Map<Long, Long> postLikeCounts = new HashMap<>();
        Map<Long, Boolean> postLikedByUser = new HashMap<>();
        Map<Long, Long> postCommentCounts = new HashMap<>();

        for (Post post : feedPosts) {
            postLikeCounts.put(post.getId(), DataStore.countLikes(post.getId()));
            postLikedByUser.put(post.getId(), DataStore.hasUserLikedPost(currentUser.getId(), post.getId()));

            long commentCount = 0;
            for (Post p : DataStore.getPosts()) {
                if (p.getParent() != null && p.getParent().getId() == post.getId()) {
                    commentCount++;
                }
            }
            postCommentCounts.put(post.getId(), commentCount);
        }

        req.setAttribute("currentUser", currentUser);
        req.setAttribute("tab", tab);
        req.setAttribute("posts", feedPosts);
        req.setAttribute("postLikeCounts", postLikeCounts);
        req.setAttribute("postLikedByUser", postLikedByUser);
        req.setAttribute("postCommentCounts", postCommentCounts);

        req.getRequestDispatcher("/WEB-INF/views/feed.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);
        String content = req.getParameter("content");

        if (content != null && !content.isBlank()) {
            Post post = new Post(currentUser, content.trim());
            DataStore.addPost(post);
        }

        String tab = req.getParameter("tab");
        resp.sendRedirect("/feed" + (tab != null ? "?tab=" + tab : ""));
    }
}
