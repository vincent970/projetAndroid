package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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


public class ViewRestaurantsActivity extends FragmentActivity implements OnMapReadyCallback,
        SensorEventListener{
    private static final String TAG = "DatabaseActivity";
    FirebaseFirestore db = null;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private List<Marker> restaurantMarkers;
    private Map<String, Map<String,Double>> restaurantsPosition;
    private SensorManager mSensorManager;
    //private SensorListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_view_restaurants);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
                }, 0);

            LocationManager locationManager = (LocationManager)
                    this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

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
        getAllRestaurantsPosition();
    }

    private void updateUserLocationOnMap(Location location ){
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentLocationMarker == null) {
            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(userLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,13));
        } else {
            currentLocationMarker.setPosition(userLocation);
        }
        //updateCameraBearing(mSensorManager.);
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
                restaurantMarkers.add(mMap.addMarker(new MarkerOptions()
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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        updateCameraBearing(sensorEvent.sensor.getResolution());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void updateCameraBearing(float bearing){
        Toast.makeText(this, "updating bearing", Toast.LENGTH_SHORT).show();
        CameraPosition position = CameraPosition
                .builder(
                        mMap.getCameraPosition()
                )
                .bearing(bearing)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
