<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.simplon.models.Post" %>
<%@ page import="fr.simplon.models.User" %>
<%@ page import="fr.simplon.models.Attachment" %>

<%
    User currentUser = (User) request.getAttribute("currentUser");
    User profileUser = (User) request.getAttribute("profileUser");
    boolean isOwnProfile = (Boolean) request.getAttribute("isOwnProfile");
    boolean isFollowing = (Boolean) request.getAttribute("isFollowing");
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    Map<Long, Long> postLikeCounts = (Map<Long, Long>) request.getAttribute("postLikeCounts");
    Map<Long, Boolean> postLikedByUser = (Map<Long, Boolean>) request.getAttribute("postLikedByUser");
    Map<Long, Long> postCommentCounts = (Map<Long, Long>) request.getAttribute("postCommentCounts");
    long followingCount = (Long) request.getAttribute("followingCount");
    long followersCount = (Long) request.getAttribute("followersCount");
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
    <header>
        <h1><a href="/feed">Miniature</a></h1>
        <nav>
            <a href="/profile"><%=currentUser.getUsername()%></a>
            <form method="post" action="/logout">
                <button type="submit">Déconnexion</button>
            </form>
        </nav>
    </header>

    <main id="profile">
        <a href="/feed">&larr; Retour au fil</a>

        <!-- En-tête du profil -->
        <section id="info">
            <span><%=profileUser.getUsername().substring(0, 1).toUpperCase()%></span>
            <h2><%=profileUser.getUsername()%></h2>
            <ul>
                <li><strong><%=posts.size()%></strong> publications</li>
                <li><strong><%=followersCount%></strong> abonnés</li>
                <li><strong><%=followingCount%></strong> abonnements</li>
            </ul>
            <% if (profileUser.getBio() != null && !profileUser.getBio().isEmpty()) { %>
                <p><%=profileUser.getBio()%></p>
            <% } else if (isOwnProfile) { %>
                <p><em>Aucune bio. Modifiez votre profil pour en ajouter une.</em></p>
            <% } %>
            <% if (!isOwnProfile) { %>
                <form method="post" action="/follow">
                    <input type="hidden" name="userId" value="<%=profileUser.getId()%>" />
                    <input type="hidden" name="redirect" value="/profile?id=<%=profileUser.getId()%>" />
                    <button type="submit" <%= isFollowing ? "class=\"following\"" : "" %>>
                        <%= isFollowing ? "Abonné" : "Suivre" %>
                    </button>
                </form>
            <% } %>
        </section>

        <!-- Formulaire d'édition du profil (uniquement pour son propre profil) -->
        <% if (isOwnProfile) { %>
            <section id="edit">
                <h3>Modifier mon profil</h3>
                <form method="post" action="/profile">
                    <label for="bio">Bio</label>
                    <textarea id="bio" name="bio" rows="3" placeholder="Décrivez-vous en quelques mots..."><%=profileUser.getBio() != null ? profileUser.getBio() : ""%></textarea>
                    <button type="submit">Enregistrer</button>
                </form>
            </section>
        <% } %>

        <!-- Publications de l'utilisateur -->
        <section id="publications">
            <h3>Publications</h3>
            <% if (posts == null || posts.isEmpty()) { %>
                <p>Aucune publication pour le moment.</p>
            <% } else { %>
                <% for (Post post : posts) {
                    long likes = postLikeCounts.get(post.getId());
                    boolean liked = postLikedByUser.get(post.getId());
                    long commentCount = postCommentCounts.get(post.getId());
                %>
                    <article>
                        <header>
                            <strong><%=profileUser.getUsername()%></strong>
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
                                <input type="hidden" name="redirect" value="/profile?id=<%=profileUser.getId()%>" />
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
            <% } %>
        </section>
    </main>
</body>
</html>
