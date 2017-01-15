package com.bibiloiu.viorel.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DisplayDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
        Double latitude = intent.getDoubleExtra("Latitude",44.42);
        Double longitude = intent.getDoubleExtra("Longitude",26.10);

        FetchPollutionTask task = new FetchPollutionTask();
        Double pollutionResult=null;
        try {
            pollutionResult = task.execute(latitude,longitude).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TextView titleView =  (TextView) findViewById(R.id.titleView);
        titleView.setTextSize(30);
        titleView.setText(title);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(20);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_details);

        if(pollutionResult !=null){
            TextView weatherView = (TextView) findViewById(R.id.weatherView);
            weatherView.setTextSize(20);
            weatherView.setText("Nivelul poluantului O3 conform Weather Map =" + pollutionResult);
        }
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}

class FetchPollutionTask extends AsyncTask<Double, Void, Double> {

    private final String LOG_TAG = FetchPollutionTask.class.getSimpleName();

    private Double getPollutionDataFromJson(String jsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_TIME = "time";
        final String OWM_LOCATION = "location";
        final String OWM_LATITUDE = "latitude";
        final String OWM_LONGITUDE = "longitude";
        final String OWM_DATA = "data";
        final String OWM_PRECISION = "precision";
        final String OWM_PRESSURE = "presure";
        final String OWM_VALUE = "value";

       Log.i(LOG_TAG, jsonStr);
        JSONObject forecastJson = new JSONObject(jsonStr);

        String time = forecastJson.getString(OWM_TIME);
        JSONObject location = forecastJson.getJSONObject(OWM_LOCATION);
        Double longitude = location.getDouble(OWM_LONGITUDE);
        Double latitude = location.getDouble(OWM_LATITUDE);

        Double data = forecastJson.getDouble(OWM_DATA);

        return data;
    }

    @Override
    protected Double doInBackground(Double... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String pollutionJsonStr = null;

        try {
            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/pollution/v1/co/";
            final String APPID_PARAM = "APPID";

            final String CURRENT = "current.json";
            CharSequence lat = params[0].toString().subSequence(0, 4);
            CharSequence longit = params[1].toString().subSequence(0, 4);


            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .encodedPath("pollution/v1/o3/"+ lat +","+ longit)
                    .appendPath(CURRENT)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build();
            Log.i(LOG_TAG, " "+builtUri);
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
                Log.i(LOG_TAG,"log ");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            pollutionJsonStr = buffer.toString();
            Log.i(LOG_TAG,pollutionJsonStr);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error ", e);
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

        return null;
    }
}
