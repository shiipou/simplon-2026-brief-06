<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>

<%
    User currentUser = (User) request.getAttribute("currentUser");
    User profileUser = (User) request.getAttribute("profileUser");
    boolean isOwnProfile = (Boolean) request.getAttribute("isOwnProfile");
    boolean isFollowing = (Boolean) request.getAttribute("isFollowing");
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    Map<Long, Long> postLikeCounts = (Map<Long, Long>) request.getAttribute("postLikeCounts");
    Map<Long, Boolean> postLikedByUser = (Map<Long, Boolean>) request.getAttribute("postLikedByUser");
    Map<Long, Long> postCommentCounts = (Map<Long, Long>) request.getAttribute("postCommentCounts");
    int followingCount = (Integer) request.getAttribute("followingCount");
    int followersCount = (Integer) request.getAttribute("followersCount");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Miniature - <%=profileUser.getUsername()%></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
    <header class="top-bar">
        <h1 class="logo"><a href="/feed">Miniature</a></h1>
        <div class="top-bar-right">
            <a href="/profile" class="current-user-link"><%=currentUser.getUsername()%></a>
            <form method="post" action="/logout" class="inline-form">
                <button type="submit" class="btn btn-danger btn-sm">Déconnexion</button>
            </form>
        </div>
    </header>

    <main>
        <a href="/feed" class="back-link">&larr; Retour au fil</a>

        <!-- En-tête du profil -->
        <section class="profile-header">
            <div class="profile-avatar"><%=profileUser.getUsername().substring(0, 1).toUpperCase()%></div>
            <div class="profile-info">
                <h2 class="profile-username"><%=profileUser.getUsername()%></h2>
                <div class="profile-stats">
                    <span><strong><%=posts.size()%></strong> publications</span>
                    <span><strong><%=followersCount%></strong> abonnés</span>
                    <span><strong><%=followingCount%></strong> abonnements</span>
                </div>
                <% if (profileUser.getBio() != null && !profileUser.getBio().isEmpty()) { %>
                    <p class="profile-bio"><%=profileUser.getBio()%></p>
                <% } else if (isOwnProfile) { %>
                    <p class="profile-bio profile-bio-empty">Aucune bio. Modifiez votre profil pour en ajouter une.</p>
                <% } %>
            </div>
            <div class="profile-action">
                <% if (!isOwnProfile) { %>
                    <form method="post" action="/follow" class="inline-form">
                        <input type="hidden" name="userId" value="<%=profileUser.getId()%>" />
                        <input type="hidden" name="redirect" value="/profile?id=<%=profileUser.getId()%>" />
                        <button type="submit" class="btn <%= isFollowing ? "btn-following" : "btn-primary" %>">
                            <%= isFollowing ? "Abonné" : "Suivre" %>
                        </button>
                    </form>
                <% } %>
            </div>
        </section>

        <!-- Formulaire d'édition du profil (uniquement pour son propre profil) -->
        <% if (isOwnProfile) { %>
            <section class="profile-edit">
                <h3>Modifier mon profil</h3>
                <form method="post" action="/profile" class="profile-edit-form">
                    <label for="bio">Bio</label>
                    <textarea id="bio" name="bio" rows="3" placeholder="Décrivez-vous en quelques mots..."><%=profileUser.getBio() != null ? profileUser.getBio() : ""%></textarea>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </form>
            </section>
        <% } %>

        <!-- Publications de l'utilisateur -->
        <section class="profile-posts">
            <h3>Publications</h3>
            <% if (posts == null || posts.isEmpty()) { %>
                <p class="empty-message">Aucune publication pour le moment.</p>
            <% } else { %>
                <div class="post-list">
                <% for (Post post : posts) {
                    long likes = postLikeCounts.get(post.getId());
                    boolean liked = postLikedByUser.get(post.getId());
                    long commentCount = postCommentCounts.get(post.getId());
                %>
                    <article class="post-card">
                        <div class="post-header">
                            <strong class="post-author"><%=profileUser.getUsername()%></strong>
                            <time class="post-date"><%=post.getCreatedAt().format(fmt)%></time>
                        </div>
                        <p class="post-content"><%=post.getContent()%></p>
                        <div class="post-actions">
                            <form method="post" action="/like" class="inline-form">
                                <input type="hidden" name="postId" value="<%=post.getId()%>" />
                                <input type="hidden" name="redirect" value="/profile?id=<%=profileUser.getId()%>" />
                                <button type="submit" class="btn-action <%= liked ? "liked" : "" %>">
                                    <%= liked ? "&#9829;" : "&#9825;" %> <%=likes%>
                                </button>
                            </form>
                            <a href="/post?id=<%=post.getId()%>" class="btn-action">
                                &#128172; <%=commentCount%>
                            </a>
                        </div>
                    </article>
                <% } %>
                </div>
            <% } %>
        </section>
    </main>
</body>
</html>
