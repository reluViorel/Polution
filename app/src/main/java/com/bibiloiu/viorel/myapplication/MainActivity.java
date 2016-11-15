package com.bibiloiu.viorel.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String EXTRA_TEXT = "HELLO";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0.0,10.0);
        final MarkerOptions title = new MarkerOptions().position(sydney).title("Marker in Sydney");

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                FetchPollutionTask task = new FetchPollutionTask();
                task.execute(arg0.getPosition().latitude,arg0.getPosition().longitude);
            }
        });

        mMap.addMarker(title);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class FetchPollutionTask extends AsyncTask<Double, Void, String[]> {

        private final String LOG_TAG = FetchPollutionTask.class.getSimpleName();

        private String[] getPollutionDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_TIME = "time";
            final String OWM_LOCATION = "location";
            final String OWM_LATITUDE = "latitude";
            final String OWM_LONGITUDE = "longitude";
            final String OWM_DATA = "data";
            final String OWM_PRECISION = "precision";
            final String OWM_PRESSURE = "presure";
            final String OWM_VALUE = "value";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            String time = forecastJson.getString(OWM_TIME);
            JSONObject location = forecastJson.getJSONObject(OWM_LOCATION);
            Double longitude = location.getDouble(OWM_LONGITUDE);
            Double latitude = location.getDouble(OWM_LATITUDE);

            JSONArray data = forecastJson.getJSONArray(OWM_DATA);

            JSONObject precision;
            JSONObject pressure;
            String[] results = new String[100];
            for (int i = 0; i < data.length(); i++) {
                JSONObject dataJSONObject = data.getJSONObject(i);
                results[i] = dataJSONObject.getString(OWM_VALUE);
            }
            System.out.println("\n\n\n\n\n\n\n"+results);

            return results;
        }

        @Override
        protected String[] doInBackground(Double... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String pollutionJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/pollution/v1/co/";
                final String APPID_PARAM = "APPID";

                final String CURRENT = "current.json";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .encodedPath("pollution/v1/co/"+params[0].toString()+","+params[1].toString())
                        .appendPath(CURRENT)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

                System.out.println("\n\n\n\n\n\n\n"+builtUri);
                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                   // Nothing to do.
                    Log.i(LOG_TAG,"ASDFASDFASDFA");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                pollutionJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getPollutionDataFromJson(pollutionJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Intent intent = new Intent(getBaseContext(), DisplayDetailsActivity.class);
            // Starting the  Activity
            intent.putExtra(Intent.EXTRA_TEXT, result[1]);
            startActivity(intent);
            Log.d("mGoogleMap1", "Activity_Calling");
        }
    }
}
