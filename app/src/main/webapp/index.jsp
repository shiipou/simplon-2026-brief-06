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
    <div class="landing-page">
        <!-- Hero -->
        <section class="landing-hero">
            <img src="${pageContext.request.contextPath}/assets/banner.webp" alt="Miniature" class="banner" />
            <div class="landing-hero-overlay">
                <h1>Miniature</h1>
                <p>Le réseau social où chaque mot compte.</p>
            </div>
        </section>

        <!-- Contenu -->
        <div class="landing-content">
            <p class="landing-tagline">
                Partagez vos idées, suivez vos amis et découvrez du contenu inspirant — le tout dans un espace simple et bienveillant.
            </p>

            <div class="features">
                <div class="feature-card">
                    <div class="feature-icon">&#128172;</div>
                    <h3>Publiez</h3>
                    <p>Partagez vos pensées avec la communauté en un clic.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">&#10084;&#65039;</div>
                    <h3>Aimez</h3>
                    <p>Montrez votre soutien avec un like sur les publications qui vous plaisent.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">&#128101;</div>
                    <h3>Suivez</h3>
                    <p>Abonnez-vous aux utilisateurs qui vous inspirent et personnalisez votre fil.</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">&#128488;&#65039;</div>
                    <h3>Commentez</h3>
                    <p>Échangez et réagissez aux publications avec des commentaires.</p>
                </div>
            </div>

            <div class="landing-cta">
                <a href="/register" class="btn btn-primary">Créer un compte</a>
                <a href="/login" class="btn btn-outline">Se connecter</a>
            </div>
        </div>
    </div>
</body>
</html>
