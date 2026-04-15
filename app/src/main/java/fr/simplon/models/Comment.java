package fr.simplon.models;

public class Comment extends Post {
    private Post parent;

    public Comment() {
        super();
    }

    public Comment(User owner, String content) {
        super(owner, content);
    }

    public Comment(User owner, String content, Post parent) {
        super(owner, content);
        this.parent = parent;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    @Override
    public boolean isComment() {
        return parent != null;
    }
}