package fr.simplon.utils;

import java.util.ArrayList;
import java.util.List;

import fr.simplon.models.Post;
import fr.simplon.models.User;

/**
 * Stockage en mémoire centralisé pour toutes les données de l'application.
 */
public abstract class DataStore {

    private static final List<User> users = new ArrayList<>();
    private static final List<Post> posts = new ArrayList<>();

    // ──── Users ────

    public static List<User> getUsers() {
        return users;
    }

    public static User getUserById(long id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    // ──── Posts ────

    public static List<Post> getPosts() {
        return posts;
    }

    public static Post getPostById(long id) {
        return posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public static void addPost(Post post) {
        posts.add(post);
    }

    // ──── Likes ────

    public static boolean hasUserLikedPost(long userId, long postId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);
        return post != null && user != null && post.isLikedBy(user);
    }

    public static long countLikes(long postId) {
        Post post = getPostById(postId);
        return post != null ? post.getLikes().size() : 0;
    }

    public static void addLike(long userId, long postId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);
        if (post != null && user != null) {
            post.addLike(user);
        }
    }

    public static void removeLike(long userId, long postId) {
        Post post = getPostById(postId);
        User user = getUserById(userId);
        if (post != null && user != null) {
            post.removeLike(user);
        }
    }

    // ──── Follows ────

    public static boolean isFollowing(long followerId, long followedId) {
        User follower = getUserById(followerId);
        User followed = getUserById(followedId);
        return follower != null && followed != null && follower.isFollowing(followed);
    }

    public static void addFollow(long followerId, long followedId) {
        User follower = getUserById(followerId);
        User followed = getUserById(followedId);
        if (follower != null && followed != null) {
            follower.addFollowing(followed);
        }
    }

    public static void removeFollow(long followerId, long followedId) {
        User follower = getUserById(followerId);
        User followed = getUserById(followedId);
        if (follower != null && followed != null) {
            follower.removeFollowing(followed);
        }
    }

    public static List<User> getFollowed(long followerId) {
        User follower = getUserById(followerId);
        return follower != null ? follower.getFollowing() : new ArrayList<>();
    }
}
