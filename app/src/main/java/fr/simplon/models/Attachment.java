package fr.simplon.models;

/**
 * Pièce jointe à un post.
 * Peut être une image (URL) ou un partage de post (référence vers un autre post).
 */
public class Attachment {
    private static long nbAttachments = 0;

    private long id;
    private String type;   // "image" ou "post"
    private String image;  // URL de l'image (si type = "image")
    private Post post;     // Post partagé (si type = "post")

    public Attachment() {
        this.id = ++nbAttachments;
    }

    /** Attachment de type image */
    public Attachment(String imageUrl) {
        this();
        this.type = "image";
        this.image = imageUrl;
    }

    /** Attachment de type partage de post */
    public Attachment(Post sharedPost) {
        this();
        this.type = "post";
        this.post = sharedPost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public boolean isImage() {
        return "image".equals(type);
    }

    public boolean isSharedPost() {
        return "post".equals(type);
    }
}
