package com.bibiloiu.viorel.myapplication;

import android.Manifest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SqlHelper db = new SqlHelper(this);
        db.deleteAllPollutionSource();

        double lat = 44.4379853;
        double lng = 25.9545552;
        for (int i = 0; i < 11; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            PollutionDbItem offsetItem = new PollutionDbItem(lat, lng, "marker");

            db.addPollutionSource(offsetItem);
        }
        double lat3 = 44.4379853;
        double lng3 = 22.9545552;
        for (int i = 0; i < 11; i++) {
            double offset = i / 110d;
            lat3 = lat3 + offset;
            lng3 = lng3 + offset;
            PollutionDbItem offsetItem = new PollutionDbItem(lat3, lng3, "marker");

            db.addPollutionSource(offsetItem);
        }

    }

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.4379853, 25.9545552), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Add ten cluster items in close proximity, for purposes of this example.
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mMap, mClusterManager));
        SqlHelper db = new SqlHelper(this);
        List<PollutionDbItem> pollutions = db.getAllPollutionSources();
        for (PollutionDbItem pollutionDbItem : pollutions) {

            Drawable drawable = getResources().getDrawable(R.drawable.airplane);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.fromBitmap(scaledBitmap);

            mClusterManager.addItem(new PollutionItem(bitmapDescriptor, pollutionDbItem.getLatitude(),
                    pollutionDbItem.getLongitude(), pollutionDbItem.getTitle(), null));
        }
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
//                FetchPollutionTask task = new FetchPollutionTask();
//                task.execute(arg0.getPosition().latitude,arg0.getPosition().longitude);

                Intent intent = new Intent(getApplicationContext(), DisplayDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, arg0.getTitle());
                startActivity(intent);
            }
        });
        mMap.setMyLocationEnabled(true);

        setUpClusterer();

    }

}
