<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Miniature - Inscription</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
    <main id="register">
        <figure>
            <img src="${pageContext.request.contextPath}/assets/banner.webp" alt="Miniature" />
        </figure>
        <h2>Inscription</h2>

        <% if (error != null) { %>
            <p role="alert"><%=error%></p>
        <% } %>

        <form method="post">
            <label for="username">Nom d'utilisateur</label>
            <input type="text" name="username" id="username" required />

            <label for="email">E-Mail</label>
            <input type="email" name="email" id="email" required />

            <label for="password">Mot de passe</label>
            <input type="password" name="password" id="password" required />

            <label for="passwordConfirm">Confirmation du mot de passe</label>
            <input type="password" name="passwordConfirm" id="passwordConfirm" required />

            <button type="submit">Créer mon compte</button>
        </form>

        <footer>
            <p>Déjà un compte ? <a href="/login">Se connecter</a></p>
            <p><a href="/">&larr; Retour à l'accueil</a></p>
        </footer>
    </main>
</body>
</html>
