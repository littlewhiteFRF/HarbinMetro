package com.frf.harbinmetro.goods;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.frf.harbinmetro.R;

import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends AppCompatActivity {

    private Toolbar goods_activity_toolbar;//顶部导航栏

    private TabLayout goodstab;//顶部上滑栏

    private ViewPager goodsviewpaper;

    List<String> mTitle;//选项卡标题
    List<Fragment> mFragment;//碎片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        goods_activity_toolbar = findViewById(R.id.goods_activity_toolbar);
        goods_activity_toolbar.setTitle("我的订单");
        goods_activity_toolbar.setLogo(R.drawable.ic_action_goods_white);
        setSupportActionBar(goods_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        goodstab = findViewById(R.id.goods_activity_tablayout);
        //goodstab.addTab(goodstab.newTab().setText("可用订单").setIcon(R.drawable.ic_action_goods_yes));
        //goodstab.addTab(goodstab.newTab().setText("历史订单").setIcon(R.drawable.ic_action_goods_no));

        goodstab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //添加选中Tab的逻辑
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //添加未选中Tab的逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //再次选中tab的逻辑
            }
        });

        mTitle = new ArrayList<>();
        mTitle.add("可用订单");
        mTitle.add("历史订单");

        mFragment = new ArrayList<>();
        mFragment.add(new YesGoodsFragment());
        mFragment.add(new NoGoodsFragment());

        goodsviewpaper = findViewById(R.id.goods_activity_viewpaper);

        goodsviewpaper.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        goodstab.setupWithViewPager(goodsviewpaper);

        //当前显示一个,预先加载一个
        goodsviewpaper.setOffscreenPageLimit(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://ToolBar最左侧的按钮叫做HomeAsUp按钮，而且id永远是android.R.id.home
                finish();//结束这个这个活动
                break;
            default:
                break;
        }
        return true;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        goodsviewpaper.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragment.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragment.size();
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mTitle.get(position);
//            }
//        });
//
//        goodstab.setupWithViewPager(goodsviewpaper);
//
//        //当前显示一个,预先加载一个
//        goodsviewpaper.setOffscreenPageLimit(1);
//    }
}
