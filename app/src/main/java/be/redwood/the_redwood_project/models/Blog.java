package be.redwood.the_redwood_project.models;

import com.parse.ParseUser;

import java.net.URL;

public class Blog {
    private String title;
    private String image;
    private String username;

    public Blog(String title, String image, String username) {
        this.title = title;
        this.image = image;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
