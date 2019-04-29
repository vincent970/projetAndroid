package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListeners();
        AskForCoarseLocationPermission();
    }

    public void setListeners(){
        findViewById(R.id.btn_gotoViewRestaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissionCoarseLocation()==true){
                    gotoViewRestaurants();
                    WindowInfoMarker windowInfoMarkerToAdd = new WindowInfoMarker("titre", "description");
                }
            }
        });


    }

    private void gotoViewRestaurants(){
        startActivity(new Intent(this, ViewRestaurantsActivity.class));
    }

    private boolean checkPermissionCoarseLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            AskForCoarseLocationPermission();
            return  false;
        }
    }

    private void AskForCoarseLocationPermission(){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);
    }
}