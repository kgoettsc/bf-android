package com.example.myapplication;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
//import com.mapbox.services.commons.geojson.Feature;
import java.util.List;


public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2dvZXR0c2NoZSIsImEiOiJjajNyaWo1MXQwMDRpMzNueDgwYjM0bzEwIn0.CtCHAtlCwki608_sA0sKxw");
        setContentView(R.layout.fragment_dashboard);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(final MapboxMap mapboxMap) {
//
//                new FeaturePullTask().execute(mapboxMap);
//
//                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng point) {
//                        PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
//                        List<Feature> features = mapboxMap.queryRenderedFeatures(pointf);
//
//
//                        if (features.size() > 0) {
//                            Feature feature = features.get(0);
//
//                            if (feature.getProperties() != null) {
//                                if (feature.getProperty("activities") != null) {
//                                    String displayString = String.format("%s - (%s)", feature.getProperty("name"), feature.getProperty("activities"));
//                                    Log.d("FC", displayString);
//                                    Toast.makeText(MapActivity.this, displayString, Toast.LENGTH_LONG).show();
//                                }
//
//                            }
//                        }
//                    }
//                });
//
//                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
//                    @Override
//                    public void onCameraChange(CameraPosition position) {
//                        Float zoomLevel = FeaturePullTask.getCircleRadius(position);
//
//                        CircleLayer circleLayer = (CircleLayer) mapboxMap.getLayer("venue-layer");
//                        circleLayer.setProperties(
//                                PropertyFactory.circleRadius(zoomLevel)
//                        );
//                    }
//                });
//
//                LocationLayerPlugin locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, null);
//                locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
//            }
//        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
