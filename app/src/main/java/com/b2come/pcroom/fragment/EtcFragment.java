package com.b2come.pcroom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.LoginSelectActivity;
import com.b2come.pcroom.activity.MainActivity;
import com.b2come.pcroom.applicationclass.Util;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.w3c.dom.Text;

/**
 * Created by kgeun on 2017. 2. 13..
 */

public class EtcFragment extends Fragment {

    View view;
    LinearLayout layoutLoginInfo;
    LinearLayout layoutNoLogin;
    TextView txtEtcUserName;
    TextView txtEtcUserEmail;

    public EtcFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_etc, container, false);

        TextView logoutBtn = (TextView) view.findViewById(R.id.btn_etc_logout);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder
                        .setMessage("정말 로그아웃 하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("로그아웃",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        layoutNoLogin.setVisibility(View.VISIBLE);
                                        layoutLoginInfo.setVisibility(View.GONE);

                                        Util.removeAppPreferences(getActivity(),"username");
                                        Util.removeAppPreferences(getActivity(),"useremail");
                                        Util.removeAppPreferences(getActivity(),"accesstoken");

                                        LoginManager.getInstance().logOut();
                                        MainActivity.mOAuthLoginModule.logout(getActivity());

                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();

            }
        });

        TextView loginBtn = (TextView) view.findViewById(R.id.btn_etc_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onResume() {

        layoutLoginInfo = (LinearLayout)view.findViewById(R.id.layoutLoginInfo);
        layoutNoLogin = (LinearLayout)view.findViewById(R.id.layoutNoLogin);
        txtEtcUserEmail = (TextView)view.findViewById(R.id.txtEtcUserEmail);
        txtEtcUserName = (TextView)view.findViewById(R.id.txtEtcUserName);

        if(Util.isLoggeed(getActivity()) == null){
            //로그인 안되어있음
            layoutNoLogin.setVisibility(View.VISIBLE);
            layoutLoginInfo.setVisibility(View.GONE);
        }
        else{
            //로그인 되어있음
            layoutNoLogin.setVisibility(View.GONE);
            layoutLoginInfo.setVisibility(View.VISIBLE);
            txtEtcUserEmail.setText(MainActivity.userEmail);
            txtEtcUserName.setText(MainActivity.userName);
        }
        super.onResume();

    }
}
