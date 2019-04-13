package com.exeinformatique.hungryforapples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListeners();
    }

    public void setListeners(){
        findViewById(R.id.btn_gotoViewRestaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoViewRestaurants();
            }
        });
    }

    private void gotoViewRestaurants(){
        startActivity(new Intent(this, ViewRestaurantsActivity.class));
    }
}
