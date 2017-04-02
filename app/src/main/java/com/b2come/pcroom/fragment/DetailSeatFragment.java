package com.b2come.pcroom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.PCCafeDetailActivity;
import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.item.PCCafeItemData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.b2come.pcroom.activity.MainActivity.currentServer;

/**
 * Created by KKLee on 2016. 11. 9..
 */

public class DetailSeatFragment extends Fragment{

    PCCafeItemData mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mData = ((PCCafeDetailActivity)getActivity()).passPCCafeData();

        View view = inflater.inflate(R.layout.fragment_detail_seat, container, false);

        return view;
    }

    @Override
    public void onResume() {
        if((Util.isLoggeed(getActivity())!=null) && mData.isInfoSeat()){
            setFragment(new DetailSeatCheckFragment());
        }
        else if((Util.isLoggeed(getActivity())!=null)){
            Bundle bundle = new Bundle();
            bundle.putInt("errmsg",3);

            Fragment errorFragment = new ErrorFragment();
            errorFragment.setArguments(bundle);
            setFragment(errorFragment);
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putInt("errmsg",2);

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
        fragmentTransaction.replace(R.id.seatInfoContainer, fragment);
        fragmentTransaction.commit();
    }

}
