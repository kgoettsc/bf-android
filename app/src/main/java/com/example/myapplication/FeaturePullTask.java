package com.example.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleStrokeWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class FeaturePullTask extends AsyncTask<MapboxMap, Void, String> {
    MapboxMap mapboxMap;

    protected String doInBackground(MapboxMap... mapboxMaps){
        mapboxMap = mapboxMaps[0];
        try {
            StringBuilder result = new StringBuilder();
            URL geoJsonUrl = new URL("https://buckfinder.herokuapp.com/api/v1/venues.json");
            HttpURLConnection conn = (HttpURLConnection) geoJsonUrl.openConnection();

            conn.setRequestProperty("Authorization", String.format("Token token=%s", "1a2b3c4d5e6f7g8h9i0j"));
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            return result.toString();
        } catch (MalformedURLException e) {
            System.out.println("YOUR URL SUCKS");
        } catch (IOException e) {
            System.out.println("YOUR CONNECTION SUCKS");
        } catch (Exception e) {
            System.out.println("SOMETHING SUCKS");
        }

        return null;
    }

    public static Float getCircleRadius(CameraPosition cameraPosition) {

        double zoomLevel = cameraPosition.zoom;

        float radius = 8f;

        if (zoomLevel >= 15) {
            radius = 30f;
        } else if(zoomLevel >= 13) {
            radius = 12f;
        }

        return radius;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        GeoJsonSource geoJsonSource = new GeoJsonSource("geojson-source", result);
        mapboxMap.getStyle().addSource(geoJsonSource);

        CircleLayer circleLayer = new CircleLayer("venue-layer", "geojson-source");
        circleLayer.withProperties(
                circleOpacity(0.8F),
                visibility(Property.VISIBLE),
                fillOutlineColor("#FFFFFF"),
                circleStrokeWidth(1f),
                circleRadius(FeaturePullTask.getCircleRadius(mapboxMap.getCameraPosition())),
                circleColor(Color.parseColor("#3794B3"))
        );

        mapboxMap.getStyle().addLayer(circleLayer);
    }
}
