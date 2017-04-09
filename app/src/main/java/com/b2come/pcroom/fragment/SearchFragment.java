package com.b2come.pcroom.fragment;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.b2come.pcroom.R;
import com.b2come.pcroom.interfaces.LocationChangeApplyListener;

import static com.b2come.pcroom.applicationclass.Util.decodeSampledBitmapFromResource;

/**
 * Created by KKLee on 2016. 11. 4..
 */

public class SearchFragment extends Fragment implements LocationChangeApplyListener {

    ImageView img[];
    SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.dashboard, menu); // 옵션메뉴 지정.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setQueryHint("두 자 이상 입력하세요");
            searchView.setOnQueryTextListener(queryTextListener);
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            if(null!=searchManager ) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            }
            // 검색필드를 항상 표시하고싶을 경우false, 아이콘으로 보이고 싶을 경우 true
            searchView.setIconifiedByDefault(true);
        }
        return;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public boolean onQueryTextSubmit(String query) {
            InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            searchView.setQuery("", false);
            searchView.setIconified(true);
            Toast.makeText(getActivity(), "search 결과", Toast.LENGTH_LONG).show();
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            // TODO Auto-generated method stub
            return false;
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.dashboard, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
    }
*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        img = new ImageView[9];

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_toolbar);
        //toolbar.inflateMenu(R.menu.dashboard);


        ImageView btnSearch= (ImageView) view.findViewById(R.id.btnSearch2);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"눌렷음",Toast.LENGTH_SHORT);
            }
        });

        init(view);


        return view;
    }

    void init(View view){
        img[0] = ((ImageView)view.findViewById(R.id.imgHomeBtnSeat));
        img[0].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat1_seat, 200, 200));

        img[1] = ((ImageView)view.findViewById(R.id.imgHomeBtnCard));
        img[1].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat2_card, 200, 200));

        img[2] = ((ImageView)view.findViewById(R.id.imgHomeBtnFood));
        img[2].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat3_food, 200, 200));

        img[3] = ((ImageView)view.findViewById(R.id.imgHomeBtnPrint));
        img[3].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat4_printer, 200, 200));

        img[4] = (ImageView)view.findViewById(R.id.imgHomeBtnSteam);
        img[4].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat5_steam, 200, 200));

        img[5] = (ImageView)view.findViewById(R.id.imgHomeBtnKeyboard);
        img[5].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat6_keyboard, 200, 200));

        img[6] = (ImageView)view.findViewById(R.id.imgHomeBtnCharger);
        img[6].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat7_power, 200, 200));

        img[7] = (ImageView)view.findViewById(R.id.imgHomeBtnParking);
        img[7].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat8_car, 200, 200));

        img[8] = (ImageView)view.findViewById(R.id.imgHomeBtnWifi);
        img[8].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat9_wifi, 200, 200));
    }


    @Override
    public void changeAddress(String newAddress) {

    }
}
