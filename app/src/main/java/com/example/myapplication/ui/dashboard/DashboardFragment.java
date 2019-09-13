package com.example.myapplication.ui.dashboard;

import android.graphics.BitmapFactory;
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
import com.example.myapplication.R;
import com.example.myapplication.Venue;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class DashboardFragment extends Fragment implements MapboxMap.OnMapClickListener, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;

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

                Style.Builder styleBuilder = new
                        Style.Builder().fromUri("mapbox://styles/kgoettsche/cjfrlbsyb67022rqug9l2k4bc")
                        .withImage("ICON_ID", BitmapFactory.decodeResource(
                                DashboardFragment.this.getResources(), R.drawable.red_marker));

                mapboxMap.setStyle(styleBuilder, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.addOnMapClickListener(DashboardFragment.this);
                        enableLocationComponent(style);
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

                VenueDialogFragment venueDialogFragment = new VenueDialogFragment(venue);
                venueDialogFragment.show(fm, "fragment_edit_name");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.app_name, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.app_name, Toast.LENGTH_LONG).show();
        }
    }
}