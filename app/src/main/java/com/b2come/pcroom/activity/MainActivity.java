package com.b2come.pcroom.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.fragment.EtcFragment;
import com.b2come.pcroom.fragment.HomeFragment;
import com.b2come.pcroom.fragment.LikeFragment;
import com.b2come.pcroom.fragment.NaviFragment;
import com.b2come.pcroom.R;
import com.b2come.pcroom.fragment.SearchFragment;
import com.b2come.pcroom.interfaces.LocationChangeApplyListener;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nhn.android.naverlogin.OAuthLogin;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity{

    final String TAG = "MainActivity";

    public final static int HOME_FRAGMENT = 0;
    public final static int NAVI_FRAGMENT = 1;
    public final static int LIKE_FRAGMENT = 2;
    public final static int SEARCH_FRAGMENT = 3;
    public final static int ETC_FRAGMENT = 4;

    public final static String GOOGLE_GEOCODE_KEY ="AIzaSyDeIBKECdGNgEBIVlcX2WFP9UoS5q9FA7E";//= "AIzaSyCFd7McEtCEBmH99KLhELkrIsFqUIiAWFA";
    public static Location lastKnownLocation;
    public static String addressText ="";
    public static boolean canUseLocation = false;
    public static String currentServer;
    public static AsyncHttpClient client;
    public BottomBar bottomBar;

    public static String userName = "";
    public static String userEmail = "";
    public static String accessToken;

    public static OAuthLogin mOAuthLoginModule;
    public final String OAUTH_CLIENT_ID = "ZfWPhijkuQRd5ntwnxxI";
    public final String OAUTH_CLIENT_SECRET = "cAhCnTmyoO";
    public final String OAUTH_CLIENT_NAME = "우리동네 PC방";


    Fragment newFragment = null;

    int mCurrentFragmentIndex;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        client = new AsyncHttpClient();
        currentServer = getString(R.string.wepc_server);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mAuth = FirebaseAuth.getInstance();

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    mCurrentFragmentIndex = HOME_FRAGMENT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                else if (tabId == R.id.tab_navi) {
                    mCurrentFragmentIndex = NAVI_FRAGMENT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                else if (tabId == R.id.tab_like) {
                    mCurrentFragmentIndex = LIKE_FRAGMENT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                else if (tabId == R.id.tab_search) {
                    mCurrentFragmentIndex = SEARCH_FRAGMENT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
                else if (tabId == R.id.tab_etc) {
                    mCurrentFragmentIndex = ETC_FRAGMENT;
                    fragmentReplace(mCurrentFragmentIndex);
                }
            }

        });

        startLocationService();

        mCurrentFragmentIndex = HOME_FRAGMENT;
        fragmentReplace(mCurrentFragmentIndex);

        Intent myIntent = getIntent();

        if (Intent.ACTION_VIEW.equals(myIntent.getAction())) {
            Uri uri = myIntent.getData();
            int newFavPCId = Integer.parseInt(uri.getQueryParameter("pcid"));
            addAndPopupNewFavPCDialog(newFavPCId);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                MainActivity.this
                ,OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        accessToken = Util.getAppPreferences(this, "accesstoken");
        userName = Util.getAppPreferences(this, "username");
        userEmail = Util.getAppPreferences(this, "useremail");

    }

    public void addAndPopupNewFavPCDialog(final int newFavPCId){
        String getOneURL = "http://" + currentServer +"/wooripc/rest/pccafeone?id=" + newFavPCId + "&user_id=0";

        client.get(getOneURL,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                View dialogView = View.inflate(MainActivity.this, R.layout.dialog_addnewfavpc, null);
                try {
                    JSONObject articleObject = (JSONObject) response.get(0);

                    if(articleObject.getInt("exist") != 0)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("안내");
                        alertDialog.setMessage("이미 즐겨찾기에 등록된 PC방입니다.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else {
                        ((TextView) dialogView.findViewById(R.id.txtFavPCCafeName)).setText(articleObject.getString("name"));
                        ((TextView) dialogView.findViewById(R.id.txtFavPCCafeAddress)).setText(articleObject.getString("loc"));
                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                        ImageView ivPoster = (ImageView) dialogView.findViewById(R.id.ivPoster);
                        dlg.setTitle("QR코드로 PC방 등록");
                        dlg.setView(dialogView);
                        dlg.setNegativeButton("닫기", null);
                        dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String postFavURL = "http://" + currentServer + "/wooripc/rest/favpccafe";
                                RequestParams params = new RequestParams();
                                params.put("user_id", 0);
                                params.put("id", newFavPCId);
                                client.post(postFavURL, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        Toast.makeText(MainActivity.this, "PC방 즐겨찾기 등록 성공", Toast.LENGTH_SHORT).show();
                                        bottomBar.getTabAtPosition(2).performClick();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Toast.makeText(MainActivity.this, statusCode + responseBody.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        dlg.show();
                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Fragment newFragment = null;
        Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        newFragment = getFragment(reqNewFragmentIndex);

        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment, newFragment);
        // Commit the transaction
        transaction.commit();

    }

    private Fragment getFragment(int idx) {

        switch (idx) {
            case HOME_FRAGMENT:
                newFragment = new HomeFragment();
                break;
            case NAVI_FRAGMENT:
                newFragment = new NaviFragment();
                break;
            case LIKE_FRAGMENT:
                newFragment = new LikeFragment();
                break;
            case SEARCH_FRAGMENT:
                newFragment = new SearchFragment();
                break;
            case ETC_FRAGMENT:
                newFragment = new EtcFragment();
                break;

            default:
                Log.d(TAG, "Unhandle case");
                break;
        }

        return newFragment;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        Log.d(gps, "aaaa");

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 (높은 정확도 사용) 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }


    public void startLocationService() {
        lastKnownLocation = new Location("dummyprovider");
        lastKnownLocation.setLatitude(37.4976614);
        lastKnownLocation.setLongitude(126.99849700000004);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (chkGpsService() == true) {
                    canUseLocation = true;
                    System.out.println("GPS 네트워크 위치 사용이 켜졌을때");

                    LocationListener locationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            try {
                                lastKnownLocation = location;
                                String locURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                                        + location.getLatitude() + "," + location.getLongitude() + "&key=" + GOOGLE_GEOCODE_KEY
                                        + "&language=ko";

                                System.out.println("locURL : " + locURL);

                                (new AsyncHttpClient()).get(locURL, null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            JSONArray result = response.getJSONArray("results");
                                            String address = ((JSONObject) result.get(4)).getString("formatted_address");
                                            addressText = address.substring(5);
                                            ((LocationChangeApplyListener) newFragment).changeAddress(addressText);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                lm.removeUpdates(this);
                            } catch (SecurityException e) {
                                System.out.println("LOCATION CHANGED EXCEPTION");
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                    };

                    long minTime = 0;
                    float minDistance = 0;

                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    try {
                        /*
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000, 1, locationListener);
                        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000, 1, locationListener);*/

                        if (manager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

                        if (manager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);


                    } catch (SecurityException e) {
                        e.printStackTrace();
                        System.out.println("requestLocationUpdates Exception 발생");
                    }
                }

                else {
                    canUseLocation = false;

                    ((LocationChangeApplyListener) newFragment).changeAddress("위치 재검색을 눌러주세요.");
                }

            }

            @Override
            public void onPermissionDenied (ArrayList < String > deniedPermissions) {
                Toast.makeText(MainActivity.this, "위치 서비스 사용이 거부되었습니다. 정상적으로 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        };


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한이 거부되어서 위치 서비스를 이용할 수 없습니다. \n\n[설정] > [권한]에서 위치 권한을 켜주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

    }

    public BottomBar getBottomBar() { return bottomBar; }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
