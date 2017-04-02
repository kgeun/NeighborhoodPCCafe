package com.b2come.pcroom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.PCCafeDetailActivity;
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

public class DetailSeatCheckFragment extends Fragment{
    AsyncHttpClient client;
    View view,blueCircle,redCircle;
    PCCafeItemData mData;
    GridLayout grid;

    public DetailSeatCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_seatcheck, container, false);
        client = new AsyncHttpClient();
        mData = ((PCCafeDetailActivity)getActivity()).passPCCafeData();

        grid =  (GridLayout) view.findViewById(R.id.gridSeatInfo);

        final LayoutInflater inflater2 = inflater;
        refreshList(inflater);

        ImageButton btnRfrsh = (ImageButton) view.findViewById(R.id.btnRfrsh);
        btnRfrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList(inflater2);
            }
        });

        return view;
    }

    public void refreshList(final LayoutInflater inflater){

        String webURL = "http://" + currentServer +"/wooripc/rest/seatinfo?id=" + mData.getId();

        System.out.println("webURL : " + webURL);
        client.get(webURL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                grid.removeAllViews();
                try{
                    for(int i=0; i<response.length(); i++){
                        JSONObject articleObject = (JSONObject) response.get(i);

                        if(articleObject.getBoolean("avail")) {
                            blueCircle = inflater.inflate(R.layout.seat_item_blue,null);

                            GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),      GridLayout.spec(GridLayout.UNDEFINED, 1f));
                            blueCircle.setLayoutParams(parem);

                            ((TextView)blueCircle.findViewById(R.id.txtSeatBlue)).setText(String.valueOf(articleObject.getInt("seat_num")));
                            grid.addView(blueCircle);
                        }
                        else{
                            redCircle = inflater.inflate(R.layout.seat_item_red,null);

                            GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),      GridLayout.spec(GridLayout.UNDEFINED, 1f));
                            redCircle.setLayoutParams(parem);

                            ((TextView)redCircle.findViewById(R.id.txtSeatRed)).setText(String.valueOf(articleObject.getInt("seat_num")));
                            grid.addView(redCircle);
                        }
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
