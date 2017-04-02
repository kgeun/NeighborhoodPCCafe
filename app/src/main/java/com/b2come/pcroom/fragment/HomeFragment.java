package com.b2come.pcroom.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.InquireRegisterActivity;
import com.b2come.pcroom.activity.LoginSelectActivity;
import com.b2come.pcroom.activity.MainActivity;
import com.b2come.pcroom.applicationclass.Util;
import com.b2come.pcroom.interfaces.LocationChangeApplyListener;
import com.facebook.AccessToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import static com.b2come.pcroom.activity.MainActivity.addressText;
import static com.b2come.pcroom.activity.MainActivity.canUseLocation;
import static com.b2come.pcroom.applicationclass.Util.decodeSampledBitmapFromResource;

/**
 * Created by KKLee on 2016. 11. 4..
 */

public class HomeFragment extends Fragment implements LocationChangeApplyListener {

        ImageView img[];
        TextView address;
        LinearLayout btnLogin;
        LinearLayout btnMyInfo;

        public HomeFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
            toolbar.setTitle("");

            init(view);

            return view;
        }

    void init(View view){

        final Fragment frag = this;
        img = new ImageView[4];

        address = (TextView) view.findViewById(R.id.txtHomeCurrentAddress);
        if(canUseLocation) {
            address.setText(addressText);
        } else {
            address.setText("위치 재검색을 눌러주세요.");
        }

        img[0] = (ImageView)view.findViewById(R.id.imgHomeTopBg1);
        img[0].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.homebg, 200, 200));

        img[1] = (ImageView)view.findViewById(R.id.imgHomeNaviBtn);
        img[1].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.navi_btn_bg, 200, 200));

        img[2] = ((ImageView)view.findViewById(R.id.imgHomeTopBg2));
        img[2].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.urban_backg, 200, 200));

        img[2] = ((ImageView)view.findViewById(R.id.imgLogoBg));
        img[2].setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.toolbarbg, 200, 200));

        ImageView favBtnImg = (ImageView)view.findViewById(R.id.imgHomeFavBtn);
        favBtnImg.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.fav_btn_bg, 200, 200));

        FrameLayout navBtn = (FrameLayout)view.findViewById(R.id.frmlayoutHomeNaviBtn);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getBottomBar().getTabAtPosition(1).performClick();
            }
        });

        FrameLayout favBtn = (FrameLayout)view.findViewById(R.id.frmlayoutHomeFavBtn);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getBottomBar().getTabAtPosition(2).performClick();
            }
        });

        TextView btnLocRsrch = (TextView) view.findViewById(R.id.txtHomeLocationResearch);
        btnLocRsrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).startLocationService();
            }
        });

        LinearLayout btnQRCode = (LinearLayout)view.findViewById(R.id.btnQRCode);
        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        IntentIntegrator integrator = new IntentIntegrator(getActivity());
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(true);
                        integrator.setOrientationLocked(false);
                        integrator.forSupportFragment(frag).initiateScan();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getActivity(), "카메라 서비스 사용이 거부되었습니다. 정상적으로 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                };

                new TedPermission(getActivity())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("권한이 거부되어서 QR코드 기능을 이용할 수 없습니다. \n\n[설정] > [권한]에서 카메라 권한을 켜주세요.")
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();
            }
        });

        LinearLayout btnInquire = (LinearLayout)view.findViewById(R.id.btnInquire);
        btnInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InquireRegisterActivity.class);
                startActivity(intent);
            }
        });


        btnLogin = (LinearLayout)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
                startActivity(intent);
            }
        });

        btnMyInfo = (LinearLayout)view.findViewById(R.id.btnMyInfo);
        btnMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "내 정보 선택", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onResume() {

        super.onResume();

        if(Util.isLoggeed(getActivity()) == null){
            //로그인 안되어있음
            btnLogin.setVisibility(View.VISIBLE);
            btnMyInfo.setVisibility(View.GONE);
        }
        else{
            //로그인 되어있음
            btnLogin.setVisibility(View.GONE);
            btnMyInfo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void changeAddress(String newAddress) {
        addressText = newAddress;

        if(address != null)
            address.setText(newAddress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "QR코드 인식이 취소되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                checkValidCode(String.valueOf(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void checkValidCode(String code){
        if(!code.startsWith("wooripc://addfav")){
            Toast.makeText(getContext(), "정상적인 QR코드값이 아닙니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            String newFavPCID = code.substring(code.indexOf("=")+1);
            ((MainActivity)getActivity()).addAndPopupNewFavPCDialog(Integer.parseInt(newFavPCID));
        }
    }

    @Override
    public void onDestroyView() {
        for(int i = 0 ; i < 3 ;i++)
            recycleBitmap(img[i]);

        super.onDestroyView();
    }

    private static void recycleBitmap(ImageView iv) {
        try {
            Drawable d = iv.getDrawable();
            if (d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable) d).getBitmap();
                b.recycle();
            }

            d.setCallback(null);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}