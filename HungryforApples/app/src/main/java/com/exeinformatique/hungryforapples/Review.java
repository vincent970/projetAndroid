package com.exeinformatique.hungryforapples;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Review {
    int starRating;
    String comment;
    String username;

    FirebaseFirestore db = null;

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

    public String getReview(){
        return this.starRating + " stars, " + this.comment + " -" + this.username;
    }

    public void writeReview(){
        Map<String, Object> data = new HashMap<>();
        data.put("Rating", this.starRating);
        data.put("Comment", this.comment);
        data.put("Username", this.username);

        db = FirebaseFirestore.getInstance();
        db.collection("Restaurants").document(
                "WiG3teUIinsq6BwSWO31")
                .collection("Reviews")
                .document()
                .set(data);
    }
}
