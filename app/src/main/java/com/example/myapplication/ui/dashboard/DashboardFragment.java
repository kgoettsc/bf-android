package com.example.myapplication.ui.dashboard;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.FeaturePullTask;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Venue;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;

import java.util.List;

public class DashboardFragment extends Fragment implements MapboxMap.OnMapClickListener {

    private DashboardViewModel dashboardViewModel;
    private MapView mapView;
    private MapboxMap mapboxMap;

    private static final String geoJsonSourceId = "geoJsonData";
    private static final String geoJsonLayerId = "venue-layer";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(requireActivity(), "pk.eyJ1Ijoia2dvZXR0c2NoZSIsImEiOiJjajNyaWo1MXQwMDRpMzNueDgwYjM0bzEwIn0.CtCHAtlCwki608_sA0sKxw");

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                DashboardFragment.this.mapboxMap = mapboxMap;
//                mapboxMap.setStyle(Style.MAPBOX_STREETS);
                Style.Builder styleBuilder = new Style.Builder().fromUri("mapbox://styles/kgoettsche/cjfrlbsyb67022rqug9l2k4bc");
                mapboxMap.setStyle(styleBuilder, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.addOnMapClickListener(DashboardFragment.this);
                    }
                });

                new FeaturePullTask().execute(mapboxMap);
            }
        });

        return root;


    }

    @Override
    public boolean onMapClick(@NonNull LatLng point)  {
        PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
        RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
        List<Feature> featureList = mapboxMap.queryRenderedFeatures(rectF, geoJsonLayerId);
        FragmentManager fm = getFragmentManager();
        Venue venue;

        if (featureList.size() > 0) {
            for (Feature feature : featureList) {
                venue = new Venue(feature.properties());
                Toast.makeText(getContext(), venue.getName(), Toast.LENGTH_SHORT).show();
                VenueDialogFragment venueDialogFragment = new VenueDialogFragment(venue);
                venueDialogFragment.show(fm, "fragment_edit_name");
            }
            return true;
        }
        return false;
    }



}