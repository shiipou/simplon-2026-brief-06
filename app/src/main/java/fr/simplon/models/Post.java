package fr.simplon.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static long nbPosts = 0;

    private long id;
    private User owner;
    private Post parent; // null = post normal, non-null = commentaire
    private String content;
    private LocalDateTime createdAt;
    private boolean isDraft;
    private List<User> likes = new ArrayList<>();

    public Post() {
        this.id = ++nbPosts;
        this.createdAt = LocalDateTime.now();
        this.isDraft = false;
    }

    public Post(User owner, String content) {
        this();
        this.owner = owner;
        this.content = content;
    }

    public Post(User owner, String content, Post parent) {
        this(owner, content);
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public boolean isComment() {
        return parent != null;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void addLike(User user) {
        if (!likes.contains(user)) {
            likes.add(user);
        }
    }

    public void removeLike(User user) {
        likes.remove(user);
    }

    public boolean isLikedBy(User user) {
        return likes.contains(user);
    }
}
