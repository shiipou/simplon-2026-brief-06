package fr.simplon.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.simplon.models.Post;
import fr.simplon.models.User;
import fr.simplon.utils.AuthUtils;
import fr.simplon.utils.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/post")
public class PostController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect("/feed");
            return;
        }

        long postId;
        try {
            postId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/feed");
            return;
        }

        Post post = DataStore.getPostById(postId);
        if (post == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Post introuvable.");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);

        // Récupérer les commentaires (posts dont le parent est ce post)
        List<Post> comments = new ArrayList<>();
        for (Post p : DataStore.getPosts()) {
            if (p.getParent() != null && p.getParent().getId() == postId) {
                comments.add(p);
            }
        }
        // Tri du plus récent au plus vieux
        comments.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        // Likes du post principal
        long likeCount = DataStore.countLikes(postId);
        boolean likedByUser = DataStore.hasUserLikedPost(currentUser.getId(), postId);

        // Est-ce que l'utilisateur suit l'auteur du post ?
        boolean isFollowing = DataStore.isFollowing(currentUser.getId(), post.getOwner().getId());

        req.setAttribute("currentUser", currentUser);
        req.setAttribute("post", post);
        req.setAttribute("comments", comments);
        req.setAttribute("likeCount", likeCount);
        req.setAttribute("likedByUser", likedByUser);
        req.setAttribute("isFollowing", isFollowing);

        req.getRequestDispatcher("/WEB-INF/views/post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);
        String postIdParam = req.getParameter("postId");
        String content = req.getParameter("content");

        if (postIdParam == null || content == null || content.isBlank()) {
            resp.sendRedirect("/feed");
            return;
        }

        long postId;
        try {
            postId = Long.parseLong(postIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/feed");
            return;
        }

        Post parentPost = DataStore.getPostById(postId);
        if (parentPost == null) {
            resp.sendRedirect("/feed");
            return;
        }

        // Créer le commentaire (post avec parent)
        Post comment = new Post(currentUser, content.trim(), parentPost);
        DataStore.addPost(comment);

        resp.sendRedirect("/post?id=" + postId);
    }
}
