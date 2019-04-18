package com.exeinformatique.hungryforapples;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListeners();
        AskForCoarseLoactionPermission();
    }

    public void setListeners(){
        findViewById(R.id.btn_gotoViewRestaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissionCoarseLocation()==true){
                    gotoViewRestaurants();
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
            AskForCoarseLoactionPermission();
            return  false;
        }

    }

    private void AskForCoarseLoactionPermission(){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 0);
        //Toast.makeText(this, "Carl you motherfucking piece of shit gangbanging cocksucka", Toast.LENGTH_LONG).show();
    }

}
