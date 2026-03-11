package fr.simplon.utils;

import fr.simplon.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public abstract class AuthUtils {

    public static User getLoggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        return getLoggedUser(req) != null;
    }
}
