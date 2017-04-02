package com.b2come.pcroom.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.b2come.pcroom.R;
import com.b2come.pcroom.fragment.DetailInfoFragment;
import com.b2come.pcroom.fragment.DetailSeatFragment;
import com.b2come.pcroom.fragment.LikeFragment;
import com.b2come.pcroom.fragment.LikeListFragment;
import com.b2come.pcroom.fragment.NaviFragment;
import com.b2come.pcroom.interfaces.PassPCCafeData;
import com.b2come.pcroom.item.PCCafeItemData;
import com.tsengvn.typekit.TypekitContextWrapper;

import static com.b2come.pcroom.fragment.LikeFragment.*;

/**
 * Created by KKLee on 2016. 11. 9..
 */

public class PCCafeDetailActivity extends AppCompatActivity implements PassPCCafeData {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    PCCafeItemData viewData;

    private int[] tabIcons = {
            R.drawable.ic_info,
            R.drawable.ic_seat
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent myIntent = getIntent();

        if (Intent.ACTION_VIEW.equals(myIntent.getAction())) {
            Uri uri = myIntent.getData();
            int pos = Integer.parseInt(uri.getQueryParameter("pos"));
            String from = uri.getQueryParameter("from");
            if(from.equals("navi")){
                viewData = (PCCafeItemData) NaviFragment.mAdapter.getItem(pos);
            }
            else if(from.equals("fav")){
                viewData = (PCCafeItemData) LikeListFragment.mAdapter.getItem(pos);
            }
        }

        setTitle(viewData.getName());

        toolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.detailPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.detailTabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

    }


    private void setupTabIcons() {
        View newTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView)newTab.findViewById(R.id.tabtext)).setText("상세정보");
        ((ImageView)newTab.findViewById(R.id.tabimage)).setImageResource(tabIcons[0]);
        tabLayout.getTabAt(0).setCustomView(newTab);

        newTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((TextView)newTab.findViewById(R.id.tabtext)).setText("좌석현황");
        ((ImageView)newTab.findViewById(R.id.tabimage)).setImageResource(tabIcons[1]);
        tabLayout.getTabAt(1).setCustomView(newTab);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailInfoFragment(), "상세정보");
        adapter.addFragment(new DetailSeatFragment(), "좌석현황");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public PCCafeItemData passPCCafeData() {
        return viewData;
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
