package be.redwood.the_redwood_project.models;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class User extends ParseUser {
    private String email;
    private String username;
    private String password;

    public User(String email, String username, String password) {
        email = this.email;
        username = this.username;
        password = this.password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
