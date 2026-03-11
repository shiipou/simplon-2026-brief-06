<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>

<%
    User currentUser = (User) request.getAttribute("currentUser");
    Post post = (Post) request.getAttribute("post");
    List<Post> comments = (List<Post>) request.getAttribute("comments");
    long likeCount = (Long) request.getAttribute("likeCount");
    boolean likedByUser = (Boolean) request.getAttribute("likedByUser");
    boolean isFollowing = (Boolean) request.getAttribute("isFollowing");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Miniature - Post</title>
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

        <!-- Post principal -->
        <article class="post-card post-detail">
            <div class="post-header">
                <a href="/profile?id=<%=post.getOwner().getId()%>" class="post-author-link"><strong class="post-author"><%=post.getOwner().getUsername()%></strong></a>
                <% if (post.getOwner().getId() != currentUser.getId()) { %>
                    <form method="post" action="/follow" class="inline-form">
                        <input type="hidden" name="userId" value="<%=post.getOwner().getId()%>" />
                        <input type="hidden" name="redirect" value="/post?id=<%=post.getId()%>" />
                        <button type="submit" class="btn-follow <%= isFollowing ? "following" : "" %>">
                            <%= isFollowing ? "Abonn&eacute;" : "Suivre" %>
                        </button>
                    </form>
                <% } %>
                <time class="post-date"><%=post.getCreatedAt().format(fmt)%></time>
            </div>
            <p class="post-content"><%=post.getContent()%></p>
            <div class="post-actions">
                <form method="post" action="/like" class="inline-form">
                    <input type="hidden" name="postId" value="<%=post.getId()%>" />
                    <input type="hidden" name="redirect" value="/post?id=<%=post.getId()%>" />
                    <button type="submit" class="btn-action <%= likedByUser ? "liked" : "" %>">
                        <%= likedByUser ? "&#9829;" : "&#9825;" %> <%=likeCount%>
                    </button>
                </form>
            </div>
        </article>

        <!-- Formulaire de commentaire -->
        <form method="post" action="/post" class="comment-form">
            <input type="hidden" name="postId" value="<%=post.getId()%>" />
            <textarea name="content" placeholder="Écrire un commentaire..." required rows="2"></textarea>
            <button type="submit" class="btn btn-primary">Commenter</button>
        </form>

        <!-- Liste des commentaires -->
        <section class="comments-section">
            <h2>Commentaires (<%=comments.size()%>)</h2>
            <% if (comments.isEmpty()) { %>
                <p class="empty-message">Aucun commentaire pour le moment.</p>
            <% } else { %>
                <% for (Post comment : comments) { %>
                    <article class="comment-card">
                        <div class="comment-header">
                            <strong><%=comment.getOwner().getUsername()%></strong>
                            <time class="post-date"><%=comment.getCreatedAt().format(fmt)%></time>
                        </div>
                        <p class="comment-content"><%=comment.getContent()%></p>
                    </article>
                <% } %>
            <% } %>
        </section>
    </main>
</body>
</html>
