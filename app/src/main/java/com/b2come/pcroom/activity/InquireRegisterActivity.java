package com.b2come.pcroom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.fragment.DetailInfoFragment;
import com.b2come.pcroom.fragment.DetailSeatFragment;
import com.b2come.pcroom.fragment.InquireJoinFragment;
import com.b2come.pcroom.fragment.InquireReportFragment;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgeun on 2017. 1. 23..
 */

public class InquireRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquireregister);

        String defaultName = getIntent().getStringExtra("name");
        String defaultAddress = getIntent().getStringExtra("address");
        String defaultMsg = getIntent().getStringExtra("msg");

        Bundle bundle = new Bundle();
        bundle.putString("name",defaultName);
        bundle.putString("address",defaultAddress);
        bundle.putString("msg",defaultMsg);

        toolbar = (Toolbar) findViewById(R.id.inquireToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.inquirePager);
        setupViewPager(viewPager,bundle);

        tabLayout = (TabLayout) findViewById(R.id.inquireTabs);
        tabLayout.setupWithViewPager(viewPager);

        //setupTabIcons();
    }

/*
    private void setupTabIcons() {
        View newTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView)newTab.findViewById(R.id.tabtext)).setText("PC방 제보하기");
        ((ImageView)newTab.findViewById(R.id.tabimage)).setImageResource(tabIcons[0]);
        tabLayout.getTabAt(0).setCustomView(newTab);

        newTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView)newTab.findViewById(R.id.tabtext)).setText("가맹문의하기");
        ((ImageView)newTab.findViewById(R.id.tabimage)).setImageResource(tabIcons[1]);
        //tabLayout.getTabAt(1).setCustomView(newTab);
    }
    */

    private void setupViewPager(ViewPager viewPager, Bundle bundle) {
        InquireRegisterActivity.ViewPagerAdapter adapter = new InquireRegisterActivity.ViewPagerAdapter(getSupportFragmentManager());
        InquireReportFragment irFragment = new InquireReportFragment();
        irFragment.setArguments(bundle);
        adapter.addFragment(irFragment, "PC방 제보하기");
        adapter.addFragment(new InquireJoinFragment(), "가맹문의");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
