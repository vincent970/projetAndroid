package com.exeinformatique.hungryforapples;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Review {
    int starRating;
    String comment;
    String username;
    String documentCode;

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

    public void writeReview(final String restaurantName){

        db = FirebaseFirestore.getInstance();

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                if (name == restaurantName) {
                                    documentCode = document.getId();
                                }
                            }
                        };
                    }
                });

        if (this.documentCode != "") {
            Map<String, Object> data = new HashMap<>();
            data.put("Rating", this.starRating);
            data.put("Comment", this.comment);
            data.put("Username", this.username);
            db.collection("Restaurants").document(this.documentCode)
                    .collection("Reviews")
                    .document()
                    .set(data);
        }

    }
}
