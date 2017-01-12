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


public class DisplayDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);

        double latitude=0;
        double longitude=10;

        FetchPollutionTask task = new FetchPollutionTask();
        task.execute(latitude,longitude);

        TextView titleView =  (TextView) findViewById(R.id.titleView);
        titleView.setTextSize(30);
        titleView.setText(title);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(20);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_details);


        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}

class FetchPollutionTask extends AsyncTask<Double, Void, String[]> {

    private final String LOG_TAG = FetchPollutionTask.class.getSimpleName();

    private String[] getPollutionDataFromJson(String forecastJsonStr) throws JSONException {

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
}
