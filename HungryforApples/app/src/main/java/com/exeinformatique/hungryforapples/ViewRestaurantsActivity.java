package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.security.Permission;


public class ViewRestaurantsActivity extends FragmentActivity
        implements OnMapReadyCallback{
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    //public static final String TAG = ViewRestaurantsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }

        setContentView(R.layout.activity_view_restaurants);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();*/
        //Toast.makeText(this,"", Toast.LENGTH_LONG).show();


      LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            putMarkerOnMap(location, "");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

   /* @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
      // mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
       // mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.8900, 1.4762);
        mMap.addMarker(new MarkerOptions().position(sydney).title("here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Acquire a reference to the system Location Manager

    // Define a listener that responds to location updates


// Register the listener with the Location Manager to receive location updates
    
    private void putMarkerOnMap(Location location, String Tag ){
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userLocation).title(Tag));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
    }











//--------------------------------------------------------------------------


    private void SetWaypointUser(Location location){
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("REEEEEEEEEEEEEE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void onConnected(@Nullable Bundle bundle) {
        Location location = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            String units = "imperial";
            //new GetWeatherTask(textView).execute(url);
        }
        if(location == null){

        }else{
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        SetWaypointUser(location);
    }


    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
