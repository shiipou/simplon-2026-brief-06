<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Si déjà connecté, aller au fil d'actualité
    if (session != null && session.getAttribute("user") != null) {
        response.sendRedirect("/feed");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Miniature - Le réseau social en miniature</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
    <main id="landing">
        <section id="hero">
            <img src="${pageContext.request.contextPath}/assets/banner.webp" alt="Miniature" />
            <header>
                <h1>Miniature</h1>
                <p>Le réseau social où chaque mot compte.</p>
            </header>
        </section>

        <section id="features">
            <p>
                Partagez vos idées, suivez vos amis et découvrez du contenu inspirant
                — le tout dans un espace simple et bienveillant.
            </p>

            <ul>
                <li>
                    <span>&#128172;</span>
                    <h3>Publiez</h3>
                    <p>Partagez vos pensées avec la communauté en un clic.</p>
                </li>
                <li>
                    <span>&#10084;&#65039;</span>
                    <h3>Aimez</h3>
                    <p>Montrez votre soutien avec un like sur les publications qui vous plaisent.</p>
                </li>
                <li>
                    <span>&#128101;</span>
                    <h3>Suivez</h3>
                    <p>Abonnez-vous aux utilisateurs qui vous inspirent et personnalisez votre fil.</p>
                </li>
                <li>
                    <span>&#128488;&#65039;</span>
                    <h3>Commentez</h3>
                    <p>Échangez et réagissez aux publications avec des commentaires.</p>
                </li>
            </ul>

            <nav>
                <a href="/register">Créer un compte</a>
                <a href="/login">Se connecter</a>
            </nav>
        </section>
    </main>
</body>
</html>
