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

@WebServlet("/follow")
public class FollowController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLoggedIn(req)) {
            resp.sendRedirect("/login");
            return;
        }

        User currentUser = AuthUtils.getLoggedUser(req);
        String userIdParam = req.getParameter("userId");

        if (userIdParam == null || userIdParam.isBlank()) {
            resp.sendRedirect("/feed");
            return;
        }

        long targetUserId;
        try {
            targetUserId = Long.parseLong(userIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("/feed");
            return;
        }

        // On ne peut pas se suivre soi-même
        if (targetUserId == currentUser.getId()) {
            resp.sendRedirect("/feed");
            return;
        }

        // Toggle follow
        if (DataStore.isFollowing(currentUser.getId(), targetUserId)) {
            DataStore.removeFollow(currentUser.getId(), targetUserId);
        } else {
            DataStore.addFollow(currentUser.getId(), targetUserId);
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
