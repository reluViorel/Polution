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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

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
        addItems();
    }

    private void addItems() {

        // Add ten cluster items in close proximity, for purposes of this example.
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));

        SqlHelper db = getInstance(this);
        List<PollutionDbItem> pollutions = getInstance(this).getAllPollutionSources();
        db.close();

        for (PollutionDbItem pollutionDbItem : pollutions) {
            Bitmap scaledBitmap = getDrawable(pollutionDbItem);
            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.fromBitmap(scaledBitmap);
            mClusterManager.addItem(new PollutionItem(bitmapDescriptor, pollutionDbItem.getLatitude(),
                    pollutionDbItem.getLongitude(), pollutionDbItem.getTitle(), null));
        }
    }

    private Bitmap getDrawable(PollutionDbItem pollutionDbItem) {
        Bitmap scaledBitmap;
        Drawable drawable;
        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        switch (pollutionDbItem.getType()) {
            case "airplane":
                drawable = getResources().getDrawable(R.drawable.airplane);
                bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                break;
            case "plant":
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
