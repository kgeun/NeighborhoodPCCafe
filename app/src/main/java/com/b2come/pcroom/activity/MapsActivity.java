package com.b2come.pcroom.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.applicationclass.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Toolbar toolbar;
    Intent intent;
    LatLng loc;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = (Toolbar) findViewById(R.id.mapsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();

        TextView title = (TextView)findViewById(R.id.txtMapsTitle);
        name = intent.getStringExtra("name");
        title.setText(name);
        TextView address = (TextView)findViewById(R.id.txtMapsAddress);
        address.setText(intent.getStringExtra("address"));

        loc = new LatLng(intent.getDoubleExtra("lat",37.4976614),intent.getDoubleExtra("lng",126.99849700000004));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings setting = mMap.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setMapToolbarEnabled(false);
        setting.setScrollGesturesEnabled(true);
        setting.setTiltGesturesEnabled(false);
        setting.setRotateGesturesEnabled(true);
        setting.setZoomGesturesEnabled(true);
        setting.setZoomControlsEnabled(true);

        LatLng currentPos = loc;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentPos)
                .zoom(16)
                .build();
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
            (new Util()).gpsAlert();
        }

        mMap.addMarker(new MarkerOptions().position(loc).title(name));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
