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
    private Map<String, Map<String,Double>> restaurantsPosition;
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        getAllRestaurantsPosition();
    }

    private void getAllRestaurantsPosition() {
        restaurantsPosition = new HashMap<>();
        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.getString("Name");
                                Map<String,Double> position = getRestaurantPosition(document);
                                if (name != null && !position.isEmpty()) {
                                    addRestaurantPosition(name, position);
                                    Log.d(TAG, "added restaurant " + name
                                            + " and it's position in Map");
                                }
                            }
                            generateMarkers();
                        } else Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void addRestaurantPosition(String name, Map<String,Double> position) {
        restaurantsPosition.put(name, position);
    }

    private void generateMarkers() {
        for (Map.Entry<String, Map<String,Double>> restaurant: restaurantsPosition.entrySet()) {
            String restaurantName = restaurant.getKey();
            Map<String,Double> location = restaurant.getValue();
            Double lat = location.get("Latitude");
            Double lng = location.get("Longitude");
            String message = restaurantName + ": " + lat + ',' + lng;
            Log.d(TAG, "showing Toast with message: " + message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, Double> getRestaurantPosition(QueryDocumentSnapshot document) {
        Map<String, Double> position = new HashMap<>();
        Map location = (HashMap) document.get("Position");
        if (location != null) {
            Double latitude = (Double) location.get("Latitude");
            Double longitude = (Double) location.get("Longitude");
            if (latitude != null && longitude != null) {
                position.put("Latitude", latitude);
                position.put("Longitude", longitude);
            }
        }
        return position;
    }
}
