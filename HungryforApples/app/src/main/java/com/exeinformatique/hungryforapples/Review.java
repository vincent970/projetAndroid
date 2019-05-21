package com.exeinformatique.hungryforapples;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Review {
    float starRating;
    String comment;
    String username;

    FirebaseFirestore db = null;

    public Review(float starRating, String comment, String username) {
        this.starRating = starRating;
        this.comment = comment;
        this.username = username;
    }

    public float getStarRating() {
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

    public void writeReview(final String restaurantName, Context context){

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
        /*
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
        */
        Toast.makeText(context, getReview(), Toast.LENGTH_LONG).show();
    }
}
