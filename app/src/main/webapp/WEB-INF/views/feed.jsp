<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>
<%@ page import="fr.simplon.models.Attachment" %>

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
    <header>
        <h1><a href="/feed">Miniature</a></h1>
        <nav>
            <a href="/profile"><%=currentUser.getUsername()%></a>
            <form method="post" action="/logout">
                <button type="submit">Déconnexion</button>
            </form>
        </nav>
    </header>

    <main id="feed">
        <!-- Onglets -->
        <nav>
            <a href="/feed?tab=recommendations" <%= "recommendations".equals(tab) ? "class=\"active\"" : "" %>>
                Recommandations
            </a>
            <a href="/feed?tab=subscriptions" <%= "subscriptions".equals(tab) ? "class=\"active\"" : "" %>>
                Abonnements
            </a>
        </nav>

        <!-- Formulaire nouveau post -->
        <form method="post" action="/feed">
            <input type="hidden" name="tab" value="<%=tab%>" />
            <textarea name="content" placeholder="Quoi de neuf ?" required rows="3"></textarea>
            <button type="submit">Publier</button>
        </form>

        <!-- Liste des posts -->
        <% if (posts == null || posts.isEmpty()) { %>
            <p>Aucun post à afficher.</p>
        <% } else { %>
            <section id="posts">
            <% for (Post post : posts) {
                long likes = postLikeCounts.get(post.getId());
                boolean liked = postLikedByUser.get(post.getId());
                long commentCount = postCommentCounts.get(post.getId());
            %>
                <article>
                    <header>
                        <a href="/profile?id=<%=post.getOwner().getId()%>"><strong><%=post.getOwner().getUsername()%></strong></a>
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
                            <input type="hidden" name="redirect" value="/feed?tab=<%=tab%>" />
                            <button type="submit" <%= liked ? "class=\"liked\"" : "" %>>
                                <%= liked ? "&#9829;" : "&#9825;" %> <%=likes%>
                            </button>
                        </form>
                        <a href="/post?id=<%=post.getId()%>">
                            &#128172; <%=commentCount%>
                        </a>
                    </footer>
                </article>
            <% } %>
            </section>
        <% } %>
    </main>
</body>
</html>
