package com.exeinformatique.hungryforapples;

public class Review {
    int starRating;
    String comment;
    String username;

    public Review(int starRating, String comment, String username) {
        this.starRating = starRating;
        this.comment = comment;
        this.username = username;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
