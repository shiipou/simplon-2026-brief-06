package fr.simplon.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static long nbUsers = 0;

    private long id;
    private String username;
    private String email;
    private String password;
    private String bio = "";
    private List<User> following = new ArrayList<>();

    public User() {
        id = ++nbUsers;
    }
    public User(String username) {
        this();
        this.username = username;
    }
    public User(String username, String email, String password) {
        this(username);
        this.email = email;
        this.password = password;
    }
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void addFollowing(User user) {
        if (!following.contains(user)) {
            following.add(user);
        }
    }

    public void removeFollowing(User user) {
        following.remove(user);
    }

    public boolean isFollowing(User user) {
        return following.contains(user);
    }
}
