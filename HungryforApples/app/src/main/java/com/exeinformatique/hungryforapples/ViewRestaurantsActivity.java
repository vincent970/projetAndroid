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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewRestaurantsActivity extends FragmentActivity
        implements OnMapReadyCallback{
    private GoogleMap mMap;

    Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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

}
