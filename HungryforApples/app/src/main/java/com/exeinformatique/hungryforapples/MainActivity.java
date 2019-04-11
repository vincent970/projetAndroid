package com.exeinformatique.hungryforapples;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DatabaseActivity";
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        Map<String, Map> restaurantsLocation = getAllRestaurantsPosition();
        for (Map.Entry<String, Map> restaurant: restaurantsLocation.entrySet()) {
            String message = restaurant.getKey() + ": " + restaurant.getValue();
            Log.d(TAG, "showing Toast with message: " + message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, Map> getAllRestaurantsPosition() {
        final Map<String, Map> restaurantsPosition = new HashMap<>();
        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.getString("Name");
                                Map position = getRestaurantPosition(document);
                                if (name != null && !position.isEmpty()) {
                                    restaurantsPosition.put(name, position);
                                    Log.d(TAG, "added restaurant " + name
                                            + " and it's position in Map");
                                }
                            }
                        } else Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return restaurantsPosition;
    }

    private Map<Double, Double> getRestaurantPosition(QueryDocumentSnapshot document) {
        Map<Double, Double> position = new HashMap<>();
        Map location = (HashMap) document.get("Position");
        if (location != null) {
            Double latitude = (Double) location.get("Latitude");
            Double longitude = (Double) location.get("Longitude");
            if (latitude != null && longitude != null)
                position.put(latitude, longitude);
        }
        return position;
    }
}
