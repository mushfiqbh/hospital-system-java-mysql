package com.clinic.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String name;
    private String contact;

    public User(int userId, String username, String password, String role, String name, String contact) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.contact = contact;
    }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getContact() { return contact; }
}