package com.b2come.pcroom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.InquireRegisterActivity;
import com.b2come.pcroom.activity.MapsActivity;
import com.b2come.pcroom.activity.PCCafeDetailActivity;
import com.b2come.pcroom.item.PCCafeItemData;

import static com.b2come.pcroom.applicationclass.Util.decodeSampledBitmapFromResource;

/**
 * Created by KKLee on 2016. 11. 9..
 */

public class DetailInfoFragment extends Fragment {
    TextView txtDetailPhone, txtDetailAddress, txtBtnDetailCall;
    PCCafeItemData mData;

    ImageView img[];
    LinearLayout detailInfo[] = new LinearLayout[9];
    int imgResource[] = {R.drawable.cat1_seat, R.drawable.cat2_card, R.drawable.cat3_food,
            R.drawable.cat4_printer, R.drawable.cat5_steam, R.drawable.cat6_keyboard,
            R.drawable.cat7_power, R.drawable.cat8_car, R.drawable.cat9_wifi };
    public DetailInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);
        mData = ((PCCafeDetailActivity)getActivity()).passPCCafeData();

        init(view);


        txtDetailPhone = (TextView)view.findViewById(R.id.txtDetailPhone);
        txtBtnDetailCall = (TextView)view.findViewById(R.id.txtBtnDetailCall);

        if(mData.getPhone().equals("") || mData.getPhone().equals("0") ||
                mData.getPhone() == null)
        {
            txtDetailPhone.setText("정보 없음");
            txtBtnDetailCall.setVisibility(View.INVISIBLE);
        }
        else{
            txtDetailPhone.setText(mData.getPhone());
        }

        txtDetailAddress = (TextView)view.findViewById(R.id.txtDetailAddress);
        txtDetailAddress.setText(mData.getAddress());

        if(!mData.isInfoSeat()) detailInfo[0].setVisibility(View.GONE);
        if(!mData.isInfoCard()) detailInfo[1].setVisibility(View.GONE);
        if(!mData.isInfoFood()) detailInfo[2].setVisibility(View.GONE);
        if(!mData.isInfoPrint()) detailInfo[3].setVisibility(View.GONE);
        if(!mData.isInfoSteam()) detailInfo[4].setVisibility(View.GONE);
        if(!mData.isInfoMKey()) detailInfo[5].setVisibility(View.GONE);
        if(!mData.isInfoCharger()) detailInfo[6].setVisibility(View.GONE);
        if(!mData.isInfoPark()) detailInfo[7].setVisibility(View.GONE);
        if(!mData.isInfoWifi()) detailInfo[8].setVisibility(View.GONE);

        LinearLayout txtBtnDetailOpenMap = (LinearLayout)view.findViewById(R.id.layoutDetailOpenMap);
        txtBtnDetailOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat",mData.getLatitude());
                intent.putExtra("lng",mData.getLongitude());
                intent.putExtra("name",mData.getName());
                intent.putExtra("address", mData.getAddress());
                startActivity(intent);
            }
        });

        LinearLayout btnAddPCCafeInfo = (LinearLayout)view.findViewById(R.id.btnAddPCCafeInfo);

        btnAddPCCafeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InquireRegisterActivity.class);
                intent.putExtra("name",mData.getName());
                intent.putExtra("address",mData.getAddress());
                startActivity(intent);
            }
        });


        return view;
    }

    void init(View view){
        img = new ImageView[9];
        img[0] = ((ImageView)view.findViewById(R.id.imgDetailSeat));
        img[0].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat1_seat, 200, 200));

        img[1] = ((ImageView)view.findViewById(R.id.imgDetailCard));
        img[1].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat2_card, 200, 200));

        img[2] = ((ImageView)view.findViewById(R.id.imgDetailFood));
        img[2].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat3_food, 200, 200));

        img[3] = ((ImageView)view.findViewById(R.id.imgDetailPrint));
        img[3].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat4_printer, 200, 200));

        img[4] = (ImageView)view.findViewById(R.id.imgDetailSteam);
        img[4].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat5_steam, 200, 200));

        img[5] = (ImageView)view.findViewById(R.id.imgDetailKeyboard);
        img[5].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat6_keyboard, 200, 200));

        img[6] = (ImageView)view.findViewById(R.id.imgDetailCharger);
        img[6].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat7_power, 200, 200));

        img[7] = (ImageView)view.findViewById(R.id.imgDetailParking);
        img[7].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat8_car, 200, 200));

        img[8] = (ImageView)view.findViewById(R.id.imgDetailWifi);
        img[8].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.cat9_wifi, 200, 200));



        detailInfo[0] = ((LinearLayout)view.findViewById(R.id.layoutInfoSeat));
        //detailInfo[0].setVisibility(View.GONE);

        detailInfo[1] = ((LinearLayout)view.findViewById(R.id.layoutInfoCard));
        //detailInfo[1].setVisibility(View.GONE);

        detailInfo[2] = ((LinearLayout)view.findViewById(R.id.layoutInfoFood));
        //detailInfo[2].setVisibility(View.GONE);

        detailInfo[3] = ((LinearLayout)view.findViewById(R.id.layoutInfoPrint));
        //detailInfo[3].setVisibility(View.GONE);

        detailInfo[4] = (LinearLayout)view.findViewById(R.id.layoutInfoSteam);
        //detailInfo[4].setVisibility(View.GONE);

        detailInfo[5] = (LinearLayout)view.findViewById(R.id.layoutInfoKeyboard);
        //detailInfo[5].setVisibility(View.GONE);

        detailInfo[6] = (LinearLayout)view.findViewById(R.id.layoutInfoCharger);
        //detailInfo[6].setVisibility(View.GONE);

        detailInfo[7] = (LinearLayout)view.findViewById(R.id.layoutInfoParking);
        //detailInfo[7].setVisibility(View.GONE);

        detailInfo[8] = (LinearLayout)view.findViewById(R.id.layoutInfoWifi);
        //detailInfo[8].setVisibility(View.GONE);
    }
}
