package com.frf.harbinmetro.metrotime;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.lineinfo.LineInfoActivity;
import com.frf.harbinmetro.metrotimeinfo.MetroTimeInfoActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;

public class MetroTimeActivity extends AppCompatActivity {

    private Toolbar metro_time_activity_toolbar;//顶部导航栏
    private CardView line1;
    private CardView line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_time);

        metro_time_activity_toolbar = findViewById(R.id.metro_time_activity_toolbar);
        metro_time_activity_toolbar.setTitle("首末班车");
        metro_time_activity_toolbar.setLogo(R.drawable.ic_action_time_white);
        setSupportActionBar(metro_time_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        line1 = findViewById(R.id.metro_time_activity_cardview1);
        line2 = findViewById(R.id.metro_time_activity_cardview2);

        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MetroTimeActivity.this, "你点击了一号线", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MetroTimeActivity.this, MetroTimeInfoActivity.class);
                intent.putExtra("url", "http://mditie.mapbar.com/haerbin/line_ditie1haoxian/");
                startActivity(intent);
            }
        });

        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MetroTimeActivity.this, "你点击了三号线", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MetroTimeActivity.this, MetroTimeInfoActivity.class);
                intent.putExtra("url", "http://mditie.mapbar.com/haerbin/line_ditie3haoxian/");
                startActivity(intent);
            }
        });
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
