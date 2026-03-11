package fr.simplon.controllers;

import java.io.IOException;

import fr.simplon.models.User;
import fr.simplon.utils.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/login", "/register", "/logout"})
public class AuthController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/logout".equals(req.getServletPath())) {
            handleLogout(req, resp);
            return;
        }

        String targetPage = switch (req.getServletPath()) {
            case "/login" -> "/WEB-INF/views/login.jsp";
            case "/register" -> "/WEB-INF/views/register.jsp";
            default -> null;
        };

        if (targetPage == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.getRequestDispatcher(targetPage).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (req.getServletPath()) {
            case "/login" -> handleLogin(req, resp);
            case "/register" -> handleRegister(req, resp);
            case "/logout" -> handleLogout(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Veuillez remplir tous les champs.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        User loggedUser = null;
        for (User user : DataStore.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                loggedUser = user;
                break;
            }
        }

        if (loggedUser == null) {
            req.setAttribute("error", "Identifiants incorrects.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", loggedUser);

        resp.sendRedirect("/feed");
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("passwordConfirm");

        if (username == null || email == null || password == null || confirm == null
                || username.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
            req.setAttribute("error", "Veuillez remplir tous les champs.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Les mots de passe ne correspondent pas.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // Vérifier si le nom d'utilisateur existe déjà
        for (User u : DataStore.getUsers()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                req.setAttribute("error", "Ce nom d'utilisateur est déjà pris.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }
        }

        User user = new User(username, email, password);
        DataStore.addUser(user);

        resp.sendRedirect("/login");
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect("/login");
    }
}
