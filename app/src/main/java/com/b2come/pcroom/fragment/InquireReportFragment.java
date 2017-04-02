package com.b2come.pcroom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.b2come.pcroom.R;

/**
 * Created by kgeun on 2017. 1. 23..
 */

public class InquireReportFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inquire_report, container, false);

        String defaultName = getArguments().getString("name");
        String defaultAddress = getArguments().getString("address");
        String defaultMsg = getArguments().getString("msg");

        EditText edttxtInquirePCCafeName = (EditText) view.findViewById(R.id.edttxtInquirePCCafeName);
        EditText edttxtInquirePCCafeAddress = (EditText) view.findViewById(R.id.edttxtInquirePCCafeAddress);
        EditText edttxtInquireOtherMsg = (EditText) view.findViewById(R.id.edttxtInquireOtherMsg);

        if(defaultName != null){
            edttxtInquirePCCafeName.setText(defaultName);
        }
        if(defaultAddress != null){
            edttxtInquirePCCafeAddress.setText(defaultAddress);
        }
        if(defaultMsg != null){
            edttxtInquireOtherMsg.setText(defaultMsg);
        }

        return view;
    }
}
