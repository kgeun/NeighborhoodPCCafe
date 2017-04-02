package com.b2come.pcroom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.InquireRegisterActivity;
import com.b2come.pcroom.activity.LoginSelectActivity;
import com.b2come.pcroom.activity.PCCafeDetailActivity;
import com.b2come.pcroom.item.PCCafeItemData;

import org.w3c.dom.Text;

/**
 * Created by kgeun on 2017. 2. 15..
 */
public class ErrorFragment extends Fragment {
    int errorCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_error, container, false);
        TextView txtErrorMsg1 = (TextView)view.findViewById(R.id.txtErrorMsg1);
        TextView txtErrorMsg2 = (TextView)view.findViewById(R.id.txtErrorMsg2);
        ImageView imgErrorStatus = (ImageView)view.findViewById(R.id.imgErrorStatus);
        TextView btnError = (TextView) view.findViewById(R.id.btnError);

        errorCode = getArguments().getInt("errmsg");


        switch(errorCode)
        {
            case 1:
                // 즐겨찾기에 로그인 안되있음
                imgErrorStatus.setImageResource(R.drawable.errorscreen);
                txtErrorMsg1.setText("로그인이 필요해요");
                txtErrorMsg2.setText("편리한 즐겨찾기 이용을 위해서\n 로그인해주세요.");

                btnError.setText("로그인 / 가입 하러가기");
                btnError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case 2:
                // 좌석 정보 로그인 안되있음
                imgErrorStatus.setImageResource(R.drawable.errorscreen);
                txtErrorMsg1.setText("로그인이 필요해요");
                txtErrorMsg2.setText("로그인하시면 이 PC방의 좌석정보를 확인 하실 수 있어요.");

                btnError.setText("로그인 / 가입 하러가기");
                btnError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LoginSelectActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 3:
                // 좌석 정보 없음
                final PCCafeItemData mData = ((PCCafeDetailActivity) getActivity()).passPCCafeData();

                imgErrorStatus.setImageResource(R.drawable.noavailscreen);
                txtErrorMsg1.setText("좌석정보가 없어요");
                txtErrorMsg2.setText("현재 좌석정보를 제공하지 않는 PC방입니다.\n저희에게 제보해주시면 어서 추가할게요!");

                btnError.setText("제보하러가기");
                btnError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), InquireRegisterActivity.class);
                        intent.putExtra("name",mData.getName());
                        intent.putExtra("address",mData.getAddress());
                        intent.putExtra("msg","이 PC방의 좌석정보를 추가해주세요.");
                        startActivity(intent);

                    }
                });
                break;
        }


        return view;
    }
}
