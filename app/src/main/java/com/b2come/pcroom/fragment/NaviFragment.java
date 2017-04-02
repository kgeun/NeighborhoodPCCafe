package com.b2come.pcroom.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.interfaces.LocationChangeApplyListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.MainActivity;
import com.b2come.pcroom.activity.PCCafeDetailActivity;
import com.b2come.pcroom.item.PCCafeItemData;
import com.b2come.pcroom.item.PCCafeListAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.b2come.pcroom.activity.MainActivity.currentServer;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by KKLee on 2016. 11. 4..
 */

public class NaviFragment extends Fragment implements OnMapReadyCallback, LocationChangeApplyListener{

    public static PCCafeListAdapter mAdapter;
    LatLng virtualLocation;
    ListView listView;
    TextView address;
    AsyncHttpClient client;
    int currentPage = 0;
    int currentBufferSize;
    boolean isArrayStable = false;
    private GoogleMap mMap;
    BitmapDescriptor icon;

    public NaviFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new AsyncHttpClient();
        MapsInitializer.initialize(getApplicationContext());
        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_mapicon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navi, container, false);

        //툴바
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);

        //지도
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //리스트
        listView = (ListView) view.findViewById(R.id.navi_list);
        mAdapter = new PCCafeListAdapter(getContext());
        listView.setAdapter(mAdapter);
        listUpdate(false, false);

        //주소설정
        address = (TextView)view.findViewById(R.id.txtNaviCurrentAddress);
        address.setText(MainActivity.addressText);

        return view;
    }

    void listUpdate(final boolean isRefreshItems, final boolean isVirtualLocation){

        double latitude = (isVirtualLocation)? virtualLocation.latitude : MainActivity.lastKnownLocation.getLatitude();
        double longitude = (isVirtualLocation)? virtualLocation.longitude : MainActivity.lastKnownLocation.getLongitude();

        String webURL = "http://" + currentServer +"/wooripc/rest/pccafelistbydistance?latitude=" + latitude
                + "&longitude=" + longitude + "&page=" + currentPage;

        System.out.println("webURL : " + webURL);

        client.get(webURL, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int i;

                if(isRefreshItems == true)
                {
                    mAdapter = new PCCafeListAdapter(getContext());
                }
                for(i=0; i<response.length(); i++) {

                    final PCCafeItemData d = new PCCafeItemData();

                    try {
                        JSONObject articleObject = (JSONObject) response.get(i);

                        d.setId(articleObject.getInt("id"));
                        d.setAddress(articleObject.getString("loc"));
                        //addressText.setDistance(articleObject.getDouble("distance"));
                        d.setPhone(articleObject.getString("phone"));
                        d.setLatitude(articleObject.getDouble("latitude"));
                        d.setLongitude(articleObject.getDouble("longitude"));
                        d.setName(articleObject.getString("name"));
                        d.setRating(articleObject.getDouble("rating"));
                        d.setDistance(
                                Util.getDist(MainActivity.lastKnownLocation.getLatitude(), MainActivity.lastKnownLocation.getLongitude(),
                                        d.getLatitude(), d.getLongitude())
                        );
                        d.setInfoSeat(articleObject.getBoolean("cat1"));
                        d.setInfoCard(articleObject.getBoolean("cat2"));
                        d.setInfoFood(articleObject.getBoolean("cat3"));
                        d.setInfoPrint(articleObject.getBoolean("cat4"));
                        d.setInfoSteam(articleObject.getBoolean("cat5"));
                        d.setInfoMKey(articleObject.getBoolean("cat6"));
                        d.setInfoCharger(articleObject.getBoolean("cat7"));
                        d.setInfoPark(articleObject.getBoolean("cat8"));
                        d.setInfoWifi(articleObject.getBoolean("cat9"));
                        d.setImageURL(articleObject.getString("image"));


                    } catch (JSONException e) {

                    } finally {
                        mAdapter.add(d);

                        LatLng pos = new LatLng(d.getLatitude(), d.getLongitude());
                        MarkerOptions newMarker = new MarkerOptions().position(pos).icon(icon).zIndex((float) i).title(d.getName());

                        if(newMarker != null )
                            mMap.addMarker(newMarker);

                    }
                }

                if (isRefreshItems == true) {
                    listView.setAdapter(mAdapter);
                }

                bindClickEvent();
                mAdapter.notifyDataSetChanged();
                isArrayStable = true;
            }
        });
    }

    private void bindClickEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url ="wooripc://viewdetail?pos=" + position + "&from=navi";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings setting = mMap.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setMapToolbarEnabled(false);
        setting.setScrollGesturesEnabled(true);
        setting.setTiltGesturesEnabled(false);
        setting.setRotateGesturesEnabled(false);
        //setting.setZoomGesturesEnabled(false);

        cameraMoveToLocation(MainActivity.lastKnownLocation);

        PermissionListener permissionlistener = new PermissionListener(){
            @Override
            public void onPermissionGranted() {
                try {
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "위치 서비스 사용이 거부되었습니다. 정상적으로 이 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한이 거부되어서 위치 서비스를 이용할 수 없습니다. \n\n[설정] > [권한]에서 위치 권한을 켜주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        cameraMoveToLocation(MainActivity.lastKnownLocation);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mMap.clear();

                virtualLocation = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);
                listUpdate(true,true);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                ((MainActivity)getActivity()).startLocationService();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                int position = (int)arg0.getZIndex();
                String url ="wooripc://viewdetail?pos=" + position + "&from=navi";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void cameraMoveToLocation(Location location) {
        LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentPos)
                .zoom(15)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void changeAddress(String newAddress) {
        try {
            address.setText(MainActivity.addressText);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }


}
