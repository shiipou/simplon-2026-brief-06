<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>

<%
    User currentUser = (User) request.getAttribute("currentUser");
    String tab = (String) request.getAttribute("tab");
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    Map<Long, Long> postLikeCounts = (Map<Long, Long>) request.getAttribute("postLikeCounts");
    Map<Long, Boolean> postLikedByUser = (Map<Long, Boolean>) request.getAttribute("postLikedByUser");
    Map<Long, Long> postCommentCounts = (Map<Long, Long>) request.getAttribute("postCommentCounts");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Miniature - Fil d'actualité</title>
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
        <!-- Onglets -->
        <nav class="tabs">
            <a href="/feed?tab=recommendations" class="tab <%= "recommendations".equals(tab) ? "active" : "" %>">
                Recommandations
            </a>
            <a href="/feed?tab=subscriptions" class="tab <%= "subscriptions".equals(tab) ? "active" : "" %>">
                Abonnements
            </a>
        </nav>

        <!-- Formulaire nouveau post -->
        <form method="post" action="/feed" class="new-post-form">
            <input type="hidden" name="tab" value="<%=tab%>" />
            <textarea name="content" placeholder="Quoi de neuf ?" required rows="3"></textarea>
            <button type="submit" class="btn btn-primary">Publier</button>
        </form>

        <!-- Liste des posts -->
        <% if (posts == null || posts.isEmpty()) { %>
            <p class="empty-message">Aucun post à afficher.</p>
        <% } else { %>
            <div class="post-list">
            <% for (Post post : posts) {
                long likes = postLikeCounts.get(post.getId());
                boolean liked = postLikedByUser.get(post.getId());
                long commentCount = postCommentCounts.get(post.getId());
            %>
                <article class="post-card">
                    <div class="post-header">
                        <a href="/profile?id=<%=post.getOwner().getId()%>" class="post-author-link"><strong class="post-author"><%=post.getOwner().getUsername()%></strong></a>
                        <time class="post-date"><%=post.getCreatedAt().format(fmt)%></time>
                    </div>
                    <p class="post-content"><%=post.getContent()%></p>
                    <div class="post-actions">
                        <form method="post" action="/like" class="inline-form">
                            <input type="hidden" name="postId" value="<%=post.getId()%>" />
                            <input type="hidden" name="redirect" value="/feed?tab=<%=tab%>" />
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
    </main>
</body>
</html>
