package fr.simplon.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static long nbPosts = 0;

    private long id;
    private User owner;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDraft;
    private List<User> likes = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();

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
        return false;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }
}
