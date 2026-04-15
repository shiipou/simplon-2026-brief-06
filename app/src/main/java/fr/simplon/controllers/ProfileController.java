package fr.simplon.controllers;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.simplon.models.Comment;
import fr.simplon.models.Post;
import fr.simplon.models.User;
import fr.simplon.utils.AuthUtils;
import fr.simplon.utils.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);

        // Déterminer quel profil afficher
        String idParam = req.getParameter("id");
        User profileUser;
        if (idParam == null || idParam.isBlank()) {
            profileUser = currentUser;
        } else {
            long profileId;
            try {
                profileId = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                resp.sendRedirect("/feed");
                return;
            }
            profileUser = DataStore.getUserById(profileId);
            if (profileUser == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur introuvable.");
                return;
            }
        }

        boolean isOwnProfile = (profileUser.getId() == currentUser.getId());
        boolean isFollowing = currentUser.isFollowing(profileUser);

        // Posts de cet utilisateur (pas les commentaires, pas les brouillons)
        List<Post> userPosts = DataStore.getPosts().stream()
                .filter(post -> post.getOwner() == profileUser && !post.isComment() && !post.isDraft())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();

        // Données supplémentaires pour les posts
        Map<Long, Long> postLikeCounts = new HashMap<>();
        Map<Long, Boolean> postLikedByUser = new HashMap<>();
        Map<Long, Long> postCommentCounts = new HashMap<>();
        for (Post post : userPosts) {
            postLikeCounts.put(post.getId(), DataStore.countLikes(post.getId()));
            postLikedByUser.put(post.getId(), DataStore.hasUserLikedPost(currentUser.getId(), post.getId()));
            long commentCount = DataStore.getPosts().stream()
                    .filter(Post::isComment)
                    .map(p -> (Comment) p)
                    .filter(c -> c.getParent() != null && c.getParent().getId() == post.getId())
                    .count();
            postCommentCounts.put(post.getId(), commentCount);
        }

        // Nombre d'abonnements et d'abonnés
        long followingCount = profileUser.getFollowing().size();
        long followersCount = DataStore.getUsers().stream()
                .filter(u -> u.isFollowing(profileUser))
                .count();

        req.setAttribute("currentUser", currentUser);
        req.setAttribute("profileUser", profileUser);
        req.setAttribute("isOwnProfile", isOwnProfile);
        req.setAttribute("isFollowing", isFollowing);
        req.setAttribute("posts", userPosts);
        req.setAttribute("postLikeCounts", postLikeCounts);
        req.setAttribute("postLikedByUser", postLikedByUser);
        req.setAttribute("postCommentCounts", postCommentCounts);
        req.setAttribute("followingCount", followingCount);
        req.setAttribute("followersCount", followersCount);

        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);

        String bio = req.getParameter("bio");
        if (bio != null) {
            currentUser.setBio(bio.trim());
        }

        resp.sendRedirect("/profile");
    }
}
