package com.b2come.pcroom.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.MainActivity;
import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.item.FavPCCafeAdapter;
import com.b2come.pcroom.item.FavPCCafeItemData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.b2come.pcroom.activity.MainActivity.currentServer;

/**
 * Created by kgeun on 2017. 2. 15..
 */

public class LikeListFragment extends Fragment {

    public static FavPCCafeAdapter mAdapter;
    ListView listView;
    AsyncHttpClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);

        client = new AsyncHttpClient();

        //리스트
        listView = (ListView) view.findViewById(R.id.fav_list);
        mAdapter = new FavPCCafeAdapter(getContext());
        listView.setAdapter(mAdapter);

        listUpdate();

        return view;
    }

    void listUpdate(){

        String webURL = "http://" + currentServer +"/wooripc/rest/favpccafe?user_id=0";

        System.out.println("webURL : " + webURL);
        client.get(webURL, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int i;

                for(i=0; i<response.length(); i++) {
                    final FavPCCafeItemData d = new FavPCCafeItemData();

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

                    } catch (JSONException e) {

                    } finally {
                        mAdapter.add(d);
                    }
                }

                mAdapter.notifyDataSetChanged();
                bindClickEvent();
            }
        });
    }


    private void bindClickEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url ="wooripc://viewdetail?pos=" + position + "&from=fav";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }



}
