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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.4379853, 25.9545552), 5));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
//        mClusterManager.setOnClusterItemInfoWindowClickListener(
//                new ClusterManager.OnClusterItemInfoWindowClickListener<PollutionItem>() {
//
//                    @Override
//                    public void onClusterItemInfoWindowClick(PollutionItem pollutionItem) {
//                        Toast.makeText(MainActivity.this,pollutionItem.getTitle(),Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(getApplicationContext(), DisplayDetailsActivity.class);
//                        intent.putExtra(Intent.EXTRA_TEXT, pollutionItem.getTitle());
//                        intent.putExtra("Latitude", pollutionItem.getPosition().latitude);
//                        intent.putExtra("Longitude", pollutionItem.getPosition().longitude);
//                        intent.putExtra("PollutionId", pollutionItem.getPosition().longitude);
//
//                        startActivity(intent);
//                    }
//                });

        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        loadData();
    }

//    private void addItems() {
//
//        // Add ten cluster items in close proximity, for purposes of this example.
//        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));
//        List<PollutionDbItem> pollutions = loadData();
//
////        SqlHelper db = getInstance(this);
////        List<PollutionDbItem> pollutions = getInstance(this).getAllPollutionSources();
////        db.close();
//
//        for (PollutionDbItem pollutionDbItem : pollutions) {
//
//            mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(getDrawable(pollutionDbItem)), pollutionDbItem.getLatitude(),
//                    pollutionDbItem.getLongitude(), pollutionDbItem.getTitle(), null));
//
//        }
//    }

    public void  loadData() {
        String tContents = "";
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));

//        SqlHelper db = getInstance(this);
//        List<PollutionDbItem> pollutions = getInstance(this).getAllPollutionSources();
//        db.close();


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

                mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(getDrawable(pollutionDbItem)), pollutionDbItem.getLatitude(),
                        pollutionDbItem.getLongitude(), pollutionDbItem.getTitle(), null));

        }

        Bitmap scaledBitmap;
        Drawable drawable;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        drawable = getResources().getDrawable(R.drawable.airplane);
        bitmapDrawable = (BitmapDrawable) drawable;
        bitmap = bitmapDrawable.getBitmap();
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap), 46.17655,21.262022,"Arad",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.521946,26.910278,"Bacau",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.658389,23.470022,"Tautii Magheraus",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.503194,26.102111,"Aurel Vlaicu",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.362222,28.488333,"Mihail Kogalniceanu",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.785167,23.686167,"Cluj Napoca",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.42,22.253333,"Caransebes",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.318139,23.888611,"Craiova",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.178492,27.620631,"Iasi",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.025278,21.9025,"Oradea",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),44.572161,26.102178,"Henri Coanda",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.785597,24.091342,"Sibiu",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.703275,22.8857,"Satu Mare",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),47.6875,26.354056,"Stefan Cel Mare",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.062486,28.714311,"Cataloi",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.467714,24.412525,"Transilvania Targu Mures",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.809861,21.337861,"Traian Vuia",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.3201,24.3157,"Aeroclub Mures",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4649,24.053,"Aeroclub Sibiu",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4649,24.053,"Aeroclub Sibiu",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.436,24.4445,"Aerodrom Cioca",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.471009,21.111967,"Aeroclub Cioca",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.4153,25.3137,"Aeroclub Ghimbav",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),45.5153,22.5813,"Aeroclub Deva",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),46.4643,23.4258,"Aeroclub Cluj",null));
        mClusterManager.addItem(new PollutionItem(BitmapDescriptorFactory.fromBitmap(scaledBitmap),43.9841995,28.6096992,"Tuzla",null));

    }

    private Bitmap getDrawable(PollutionDbItem pollutionDbItem) {
        Bitmap scaledBitmap;
        Drawable drawable;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        switch (pollutionDbItem.getType()) {
            case "Background":
                drawable = getResources().getDrawable(R.drawable.background);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                break;
            case "Industrial":
                drawable = getResources().getDrawable(R.drawable.factory);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
                break;
            case "Traffic":
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
                Toast.makeText(MainActivity.this,
                        arg0.getTitle(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DisplayDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, arg0.getTitle());
                intent.putExtra("Latitude", arg0.getPosition().latitude);
                intent.putExtra("Longitude", arg0.getPosition().longitude);
                intent.putExtra("PollutionId", arg0.getPosition().longitude);

                startActivity(intent);
            }
        });
        mMap.setMyLocationEnabled(true);
        setUpCluster();
    }

}
