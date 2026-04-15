<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>
<%@ page import="fr.simplon.models.Attachment" %>

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
    <header>
        <h1><a href="/feed">Miniature</a></h1>
        <nav>
            <a href="/profile"><%=currentUser.getUsername()%></a>
            <form method="post" action="/logout">
                <button type="submit">Déconnexion</button>
            </form>
        </nav>
    </header>

    <main id="post-detail">
        <a href="/feed">&larr; Retour au fil</a>

        <!-- Post principal -->
        <article>
            <header>
                <a href="/profile?id=<%=post.getOwner().getId()%>"><strong><%=post.getOwner().getUsername()%></strong></a>
                <% if (post.getOwner().getId() != currentUser.getId()) { %>
                    <form method="post" action="/follow">
                        <input type="hidden" name="userId" value="<%=post.getOwner().getId()%>" />
                        <input type="hidden" name="redirect" value="/post?id=<%=post.getId()%>" />
                        <button type="submit" <%= isFollowing ? "class=\"following\"" : "" %>>
                            <%= isFollowing ? "Abonn&eacute;" : "Suivre" %>
                        </button>
                    </form>
                <% } %>
                <time datetime="<%=post.getCreatedAt()%>"><%=post.getCreatedAt().format(fmt)%></time>
            </header>
            <p><%=post.getContent()%></p>
            <% for (Attachment att : post.getAttachments()) { %>
                <% if (att.isImage()) { %>
                    <figure>
                        <img src="<%=att.getImage()%>" alt="Pièce jointe" />
                    </figure>
                <% } else if (att.isSharedPost() && att.getPost() != null) { %>
                    <aside>
                        <header>
                            <a href="/profile?id=<%=att.getPost().getOwner().getId()%>"><strong><%=att.getPost().getOwner().getUsername()%></strong></a>
                            <time datetime="<%=att.getPost().getCreatedAt()%>"><%=att.getPost().getCreatedAt().format(fmt)%></time>
                        </header>
                        <p><%=att.getPost().getContent()%></p>
                        <a href="/post?id=<%=att.getPost().getId()%>">Voir le post original &rarr;</a>
                    </aside>
                <% } %>
            <% } %>
            <footer>
                <form method="post" action="/like">
                    <input type="hidden" name="postId" value="<%=post.getId()%>" />
                    <input type="hidden" name="redirect" value="/post?id=<%=post.getId()%>" />
                    <button type="submit" <%= likedByUser ? "class=\"liked\"" : "" %>>
                        <%= likedByUser ? "&#9829;" : "&#9825;" %> <%=likeCount%>
                    </button>
                </form>
            </footer>
        </article>

        <!-- Formulaire de commentaire -->
        <form method="post" action="/post">
            <input type="hidden" name="postId" value="<%=post.getId()%>" />
            <textarea name="content" placeholder="Écrire un commentaire..." required rows="2"></textarea>
            <button type="submit">Commenter</button>
        </form>

        <!-- Liste des commentaires -->
        <section id="comments">
            <h2>Commentaires (<%=comments.size()%>)</h2>
            <% if (comments.isEmpty()) { %>
                <p>Aucun commentaire pour le moment.</p>
            <% } else { %>
                <% for (Post comment : comments) { %>
                    <article>
                        <header>
                            <strong><%=comment.getOwner().getUsername()%></strong>
                            <time datetime="<%=comment.getCreatedAt()%>"><%=comment.getCreatedAt().format(fmt)%></time>
                        </header>
                        <p><%=comment.getContent()%></p>
                    </article>
                <% } %>
            <% } %>
        </section>
    </main>
</body>
</html>
