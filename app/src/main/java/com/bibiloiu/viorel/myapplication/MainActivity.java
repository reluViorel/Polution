package com.bibiloiu.viorel.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.io.InputStream;

import database.PollutionDbItem;
import database.SqlHelper;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ClusterManager<PollutionItem> mClusterManager;
    private static SqlHelper helperSqlInstance;

    public static synchronized SqlHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (helperSqlInstance == null) {
            helperSqlInstance = new SqlHelper(context.getApplicationContext());
        }
        return helperSqlInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getInstance(this);
    }

    private void setUpCluster() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.4379853, 25.9545552), 7));

        mClusterManager = new ClusterManager<>(this, mMap);


        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        loadData();
    }



    public void  loadData() {
        String tContents = "";
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));

        try {
            InputStream stream = this.getResources().openRawResource(R.raw.processed); // you will get the method getAssets anywhere from current activity.
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        String[] ary = tContents.split("\n");
        int i=1;
        for (String line : ary) {
            
                String[] details = line.split(",");

                Integer id = Integer.valueOf(details[0]);
                String title = details[1];
                String airPollutant = details[2];
                double airPollutionLevel = Double.valueOf(details[3]);
                double exceedanceThreashold = Double.valueOf(details[4]);
                double latitude = Double.valueOf(details[5]);
                double longitude = Double.valueOf(details[6]);
                String type = details[7];

                PollutionDbItem pollutionDbItem = new PollutionDbItem(latitude, longitude, title, type,
                        airPollutant, airPollutionLevel, exceedanceThreashold);
                pollutionDbItem.setId(id);

                mClusterManager.addItem(
                        new PollutionItem(BitmapDescriptorFactory.fromBitmap(getDrawable(pollutionDbItem)),
                                pollutionDbItem.getLatitude(),
                        pollutionDbItem.getLongitude(), pollutionDbItem.getTitle(),
                                "Factorul de poluare este " +airPollutant + " actual fiind " + airPollutionLevel + " depasind limita de "+exceedanceThreashold));

        }
        mClusterManager.cluster();


        Bitmap scaledBitmap;
        Drawable drawable;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        drawable = getResources().getDrawable(R.drawable.airplane);
        bitmapDrawable = (BitmapDrawable) drawable;
        bitmap = bitmapDrawable.getBitmap();
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.17655,21.262022,"Arad","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.521946,26.910278,"Bacau","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.658389,23.470022,"Tautii Magheraus","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.503194,26.102111,"Aurel Vlaicu","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.362222,28.488333,"Mihail Kogalniceanu","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.785167,23.686167,"Cluj Napoca","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.42,22.253333,"Caransebes","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.318139,23.888611,"Craiova","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.178492,27.620631,"Iasi","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.025278,21.9025,"Oradea","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.572161,26.102178,"Henri Coanda","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.785597,24.091342,"Sibiu","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.703275,22.8857,"Satu Mare","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.6875,26.354056,"Stefan Cel Mare","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.062486,28.714311,"Cataloi","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.467714,24.412525,"Transilvania Targu Mures","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.809861,21.337861,"Traian Vuia","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.3201,24.3157,"Aeroclub Mures","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4649,24.053,"Aeroclub Sibiu","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4649,24.053,"Aeroclub Sibiu","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.436,24.4445,"Aerodrom Cioca","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.471009,21.111967,"Aeroclub Cioca","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4153,25.3137,"Aeroclub Ghimbav","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.5153,22.5813,"Aeroclub Deva","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.4643,23.4258,"Aeroclub Cluj","Aeroport sursa de poluare"));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),43.9841995,28.6096992,"Tuzla","Aeroport sursa de poluare"));
        mClusterManager.cluster();

    }

    private Bitmap getDrawable(PollutionDbItem pollutionDbItem) {
        Bitmap scaledBitmap;
        Drawable drawable;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        switch (pollutionDbItem.getType()) {
            case "Background":
                drawable = getResources().getDrawable(R.drawable.back);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
                break;
            case "Industrial":
                drawable = getResources().getDrawable(R.drawable.factory);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.factory);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
                break;
        }
        return scaledBitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent intent = new Intent(getApplicationContext(), DisplayDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, arg0.getSnippet());
                intent.putExtra(Intent.EXTRA_TITLE, arg0.getTitle());

                intent.putExtra("Latitude", arg0.getPosition().latitude);
                intent.putExtra("Longitude", arg0.getPosition().longitude);
                intent.putExtra("PollutionId", arg0.getPosition().longitude);

                Toast.makeText(MainActivity.this,
                        arg0.getTitle(),
                        Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
        mMap.setMyLocationEnabled(true);
        setUpCluster();
    }

}
