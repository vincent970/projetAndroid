package com.exeinformatique.hungryforapples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMarker {

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));
    }

    public class MarkerDemoActivity extends FragmentActivity implements
            GoogleMap.OnMarkerClickListener,
            OnMapReadyCallback {

        private  final LatLng PointVirgule = new LatLng(46.122208, -70.670354);
        private  final LatLng ChezGerard = new LatLng(46.099645, -70.653726);
        private  final LatLng PoutineDor = new LatLng(46.119221, -70.683651);
        private  final LatLng TableJunior = new LatLng(46.125399, -70.684117);


        private Marker mPointVirgule;
        private Marker mChezGerard;
        private Marker mPoutineDor;
        private Marker mTableJunior;

        private GoogleMap mMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.marker_restaurant);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        /** Called when the map is ready. */
        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;

            mPointVirgule = mMap.addMarker(new MarkerOptions()
                    .position(PointVirgule)
                    .title("PointVirgule"));
            mPointVirgule.setTag(0);

            mChezGerard = mMap.addMarker(new MarkerOptions()
                    .position(ChezGerard)
                    .title("Chez Gerard"));
            mChezGerard.setTag(0);

            mPoutineDor = mMap.addMarker(new MarkerOptions()
                    .position(PoutineDor)
                    .title("Poutine D'or"));
            mPoutineDor.setTag(0);

            mTableJunior = mMap.addMarker(new MarkerOptions()
                    .position(TableJunior)
                    .title("Table junior"));
            mTableJunior.setTag(0);

            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(this);
        }

        /** Called when the user clicks a marker. */
        @Override
        public boolean onMarkerClick(final Marker marker) {

            Integer clickCount = (Integer) marker.getTag();

            if (clickCount != null) {
                clickCount = clickCount + 1;
                marker.setTag(clickCount);
                Toast.makeText(this,
                        marker.getTitle() +
                                " has been clicked " + clickCount + " times.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
