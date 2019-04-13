package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewRestaurantsActivity extends FragmentActivity
        implements OnMapReadyCallback{
    private GoogleMap mMap;

    Marker currentLocationMarker;
    private static final String TAG = "DatabaseActivity";
    private Map<String, Map<String,Double>> restaurantsPosition;
    private List<Marker> restaurantMarkers;
    FirebaseFirestore db = null;
    MarkerOptions currentLocationMarker = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }

        setContentView(R.layout.activity_view_restaurants);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

      LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateUserLocationOnMap(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.8900, 1.4762);
        MarkerOptions currentLocation = new MarkerOptions().position(sydney);
        currentLocationMarker = mMap.addMarker(currentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    private void updateUserLocationOnMap(Location location ){
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker.setPosition(userLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,13));
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
        restaurantMarkers = new ArrayList<>();
        for (Map.Entry<String, Map<String,Double>> restaurant: restaurantsPosition.entrySet()) {
            String restaurantName = restaurant.getKey();
            Map<String,Double> location = restaurant.getValue();
            Double lat = location.get("Latitude");
            Double lng = location.get("Longitude");
            if (lat != null && lng != null) {
                String message = restaurantName + ": " + lat + ',' + lng;
                Log.d(TAG, "showing Toast with message: " + message);
                restaurantMarkers.add(mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(restaurantName)));
            }
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
