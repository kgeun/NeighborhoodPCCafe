package com.b2come.pcroom.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.b2come.pcroom.R;

/**
 * Created by KKLee on 2016. 11. 9..
 */

public class PCCafeItemView extends FrameLayout {

    PCCafeItemData mData;
    ImageView imgPCCafeTnil;
    TextView txtPCCafeDistance, txtPCCafeTitle, txtPCCafeAddress, txtPCCafeInfoSeat, txtPCCafeInfoCard,
            txtPCCafeInfoFood, txtPCCafeInfoPrinter, txtPCCafeInfoSteam, txtPCCafeRating, txtPCCafeReview;
    ProgressBar pbar;

    public PCCafeItemView(Context context) {
        super(context);
        init();
    }

    public PCCafeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.pccafelist_item,this);
        imgPCCafeTnil = (ImageView) findViewById(R.id.imgPCCafeTnil);
        txtPCCafeDistance = (TextView) findViewById(R.id.txtPCCafeDistance);
        txtPCCafeTitle = (TextView) findViewById(R.id.txtPCCafeTitle);
        txtPCCafeAddress = (TextView) findViewById(R.id.txtPCCafeAddress);
        txtPCCafeInfoSeat = (TextView) findViewById(R.id.txtPCCafeInfoSeat);
        txtPCCafeInfoCard = (TextView) findViewById(R.id.txtPCCafeInfoCard);
        txtPCCafeInfoFood = (TextView) findViewById(R.id.txtPCCafeInfoFood);
        txtPCCafeInfoPrinter = (TextView) findViewById(R.id.txtPCCafeInfoPrinter);
        txtPCCafeInfoSteam = (TextView) findViewById(R.id.txtPCCafeInfoSteam);
        txtPCCafeRating = (TextView) findViewById(R.id.txtPCCafeRating);
        txtPCCafeReview = (TextView) findViewById(R.id.txtPCCafeReview);
        pbar = (ProgressBar) findViewById(R.id.pbarPCCafeTnil);
    }

    public void setPCCafeItemData(PCCafeItemData data) {
        this.mData = data;
        imgPCCafeTnil.setVisibility(View.GONE);

        Glide
                .with(getContext())
                .load(mData.getImageURL())
                .error(R.drawable.noimage)
                .centerCrop()
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        pbar.setVisibility(View.GONE);
                        imgPCCafeTnil.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbar.setVisibility(View.GONE);
                        imgPCCafeTnil.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imgPCCafeTnil);


        txtPCCafeTitle.setText(mData.getName());
        txtPCCafeAddress.setText(mData.getAddress());
        txtPCCafeRating.setText(Double.toString(mData.getRating()));

        if(mData.isInfoSeat()){
            txtPCCafeInfoSeat.setBackgroundResource(R.drawable.bg_list_att_active);
            txtPCCafeInfoSeat.setTextColor(getResources().getColor(R.color.colorPrimaryAccent));
        }
        else{
            txtPCCafeInfoSeat.setBackgroundResource(R.drawable.bg_list_att_inactive);
            txtPCCafeInfoSeat.setTextColor(getResources().getColor(R.color.colorDisabled));
        }

        if(mData.isInfoCard()){
            txtPCCafeInfoCard.setBackgroundResource(R.drawable.bg_list_att_active);
            txtPCCafeInfoCard.setTextColor(getResources().getColor(R.color.colorPrimaryAccent));
        }
        else{
            txtPCCafeInfoCard.setBackgroundResource(R.drawable.bg_list_att_inactive);
            txtPCCafeInfoCard.setTextColor(getResources().getColor(R.color.colorDisabled));
        }

        if(mData.isInfoFood()){
            txtPCCafeInfoFood.setBackgroundResource(R.drawable.bg_list_att_active);
            txtPCCafeInfoFood.setTextColor(getResources().getColor(R.color.colorPrimaryAccent));
        }
        else{
            txtPCCafeInfoFood.setBackgroundResource(R.drawable.bg_list_att_inactive);
            txtPCCafeInfoFood.setTextColor(getResources().getColor(R.color.colorDisabled));
        }

        if(mData.isInfoPrint()){
            txtPCCafeInfoPrinter.setBackgroundResource(R.drawable.bg_list_att_active);
            txtPCCafeInfoPrinter.setTextColor(getResources().getColor(R.color.colorPrimaryAccent));
        }
        else{
            txtPCCafeInfoPrinter.setBackgroundResource(R.drawable.bg_list_att_inactive);
            txtPCCafeInfoPrinter.setTextColor(getResources().getColor(R.color.colorDisabled));
        }

        if(mData.isInfoSteam()){
            txtPCCafeInfoSteam.setBackgroundResource(R.drawable.bg_list_att_active);
            txtPCCafeInfoSteam.setTextColor(getResources().getColor(R.color.colorPrimaryAccent));
        }
        else{
            txtPCCafeInfoSteam.setBackgroundResource(R.drawable.bg_list_att_inactive);
            txtPCCafeInfoSteam.setTextColor(getResources().getColor(R.color.colorDisabled));
        }

        if(mData.getDistance() >= 1){
            int temp = (int)(mData.getDistance() * 100);
            txtPCCafeDistance.setText( (double)temp / 100+ "km");
        }
        else{
            int temp = (int)(mData.getDistance() * 1000);
            txtPCCafeDistance.setText( temp+ "m" );
        }

    }

}
