package models;
public class User {
    public String username;
    public String password;
    public boolean approved;

    public User(String username, String password, boolean approved) {
        this.username = username;
        this.password = password;
        this.approved = approved;
    }
}
