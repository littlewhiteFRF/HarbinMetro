package com.frf.harbinmetro.main.newsfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frf.harbinmetro.R;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private TabLayout tabLayout = null;
    private ViewPager viewPager;
    private List<Fragment> mfragmentList;
    private String[] mTabTitles = new String[8];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.tab_viewpager);

        mTabTitles[0] = "头条";
        mTabTitles[1] = "社会";
        mTabTitles[2] = "娱乐";
        mTabTitles[3] = "体育";
        mTabTitles[4] = "财经";
        mTabTitles[5] = "科技";
        mTabTitles[6] = "军事";
        mTabTitles[7] = "时尚";
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        mfragmentList = new ArrayList<>();
        mfragmentList.add(new TopFragment());//头条
        mfragmentList.add(new SocietyFragment());//社会
        mfragmentList.add(new EntertainmentFragment());//娱乐
        mfragmentList.add(new SportsFragment());//体育
        mfragmentList.add(new FinanceFragment());//财经
        mfragmentList.add(new TechnologyFragment());//科技
        mfragmentList.add(new MilitaryFragment());//军事
        mfragmentList.add(new FashionFragment());//时尚

        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //将ViewPager和TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(8);
        //getChildFragmentManager().beginTransaction().replace(R.id.tab_viewpager,mfragmentList.get(0)).show(mfragmentList.get(0)).commit();
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mfragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mfragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }
}
