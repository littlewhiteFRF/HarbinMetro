package com.frf.harbinmetro.aboutus;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.frf.harbinmetro.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {

    private Toolbar about_us_activity_toolbar;//顶部导航栏

    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        about_us_activity_toolbar = findViewById(R.id.about_us_activity_toolbar);
        about_us_activity_toolbar.setTitle("关于我们");
        about_us_activity_toolbar.setLogo(R.drawable.ic_action_about_us_white);
        setSupportActionBar(about_us_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        circleImageView = findViewById(R.id.about_us_activity_imageview);
        circleImageView.setImageDrawable(getDrawable(R.mipmap.harbin_metro_logo_round));
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
}
