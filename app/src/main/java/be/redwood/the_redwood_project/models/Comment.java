package be.redwood.the_redwood_project.models;

import java.util.Date;

public class Comment {
    private String commentText;
    private String username;
    private Date date;

    public Comment(String commentText, String username, Date date) {
        this.commentText = commentText;
        this.username = username;
        this.date = date;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
