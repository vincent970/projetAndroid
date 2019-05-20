package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewRestaurantsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "DatabaseActivity";
    FirebaseFirestore db = null;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private List<Marker> restaurantMarkers;
    private Map<String, Map<String,Double>> restaurantsPosition;

    final Context context = this;

    Map<String,Double> location;
    String restaurantName;
    String description;
    String heure_ouverture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_view_restaurants);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION
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
             restaurantName  = restaurant.getKey();
             location = restaurant.getValue();
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_info_window);

                String adresse = "";

                adresse +=  DecoderAdresse(marker.getPosition().latitude,marker.getPosition().longitude);
                description = getRestaurantDescription(marker.getTitle());

                TextView textTitre = dialog.findViewById(R.id.textView_titre);
                TextView textDescription = dialog.findViewById(R.id.textView_description);
                TextView textAdresse = dialog.findViewById(R.id.textView_adresse);
                TextView textHeureOuverture = dialog.findViewById(R.id.textView_heure_ouverture);

                textTitre.setText(marker.getTitle());
                textDescription.setText(description);
                textAdresse.setText(adresse);
                textHeureOuverture.setText(getRestaurantHeureOuverture(marker.getTitle()));

                dialog.show();
                return false;
            }
        });
    }

    private String DecoderAdresse(Double latitude, Double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

       try
       {
           addresses = geocoder.getFromLocation(latitude, longitude, 1);

           String address = addresses.get(0).getAddressLine(0);

           return address;
       }

       catch(IOException e)
        {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private String getRestaurantDescription(final String restaurantName)
    {
        //description = ;
        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String nameToCompare = restaurantName;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name").toString();
                                if (name == nameToCompare) {
                                    description = document.getString("Type");

                                }

                            }
                        } else Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return description;
    }

    private String getRestaurantHeureOuverture(final String restaurantName)
    {
        heure_ouverture = "8:00";
        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String nameToCompare = restaurantName;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                                if (name == nameToCompare) {
                                    heure_ouverture = document.getString("Ouverture");

                                }
                            }
                            generateMarkers();
                        } else Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return heure_ouverture;
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