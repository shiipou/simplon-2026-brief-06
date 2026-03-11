package fr.simplon.controllers;

import java.io.IOException;

import fr.simplon.models.User;
import fr.simplon.utils.AuthUtils;
import fr.simplon.utils.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/like")
public class LikeController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);
        String postIdParam = req.getParameter("postId");

        if (postIdParam == null || postIdParam.isBlank()) {
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

        // Toggle like
        if (DataStore.hasUserLikedPost(currentUser.getId(), postId)) {
            DataStore.removeLike(currentUser.getId(), postId);
        } else {
            DataStore.addLike(currentUser.getId(), postId);
        }

        // Rediriger vers la page d'origine
        String redirectTo = req.getParameter("redirect");
        if (redirectTo != null && !redirectTo.isBlank()) {
            resp.sendRedirect(redirectTo);
        } else {
            resp.sendRedirect("/feed");
        }
    }
}
