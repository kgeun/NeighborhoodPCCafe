package com.b2come.pcroom.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.MainActivity;
import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.interfaces.LocationChangeApplyListener;
import com.b2come.pcroom.item.FavPCCafeAdapter;
import com.b2come.pcroom.item.FavPCCafeItemData;
import com.b2come.pcroom.item.PCCafeItemData;
import com.b2come.pcroom.item.PCCafeListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.b2come.pcroom.activity.MainActivity.currentServer;

/**
 * Created by KKLee on 2016. 11. 4..
 */

public class LikeFragment extends Fragment{

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_like, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.like_toolbar);
        toolbar.setTitle("");

        return view;
    }

    @Override
    public void onResume() {
        if(Util.isLoggeed(getActivity()) !=null){
            setFragment(new LikeListFragment());
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putInt("errmsg",1);

            Fragment errorFragment = new ErrorFragment();
            errorFragment.setArguments(bundle);
            setFragment(errorFragment);
        }
        super.onResume();
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.like_container, fragment);
        fragmentTransaction.commit();
    }
}
