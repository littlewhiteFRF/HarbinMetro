package com.frf.harbinmetro.lostfindinfo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.util.Utils;

public class LostFindInfoActivity extends AppCompatActivity {

    private Toolbar lost_find_info_activity_toolbar;//顶部导航栏

    private TextView title;//标题
    private ImageView photo;//照片
    private TextView feature;//特征
    private TextView date;//拾取时间
    private TextView address;//拾取地点
    private TextView publisher;//发布者

    private LostFindInfo lostFindInfo;//失物招领信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find_info);

        lost_find_info_activity_toolbar = findViewById(R.id.lost_find_info_activity_toolbar);
        lost_find_info_activity_toolbar.setTitle("失物招领详情");
        lost_find_info_activity_toolbar.setLogo(R.drawable.ic_action_lost_find);
        setSupportActionBar(lost_find_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        title = findViewById(R.id.lost_find_info_activity_title);//标题
        photo = findViewById(R.id.lost_find_info_activity_imageview);//照片
        feature = findViewById(R.id.lost_find_info_activity_feature);//特征
        date = findViewById(R.id.lost_find_info_activity_date);//拾取时间
        address = findViewById(R.id.lost_find_info_activity_address);//拾取地点
        publisher = findViewById(R.id.lost_find_info_activity_publisher);//发布者

        Bundle bundle = (Bundle) getIntent().getExtras().get("bundle");
        lostFindInfo = new LostFindInfo();
        lostFindInfo = (LostFindInfo) bundle.getParcelable("onelostfind");

        title.setText(lostFindInfo.getTitle().toString());
        Glide.with(LostFindInfoActivity.this).load(lostFindInfo.getImageurl().toString()).into(photo);//一个图片加载框架
        feature.setText(lostFindInfo.getFeature().toString());
        date.setText(lostFindInfo.getDateTime().toString());
        address.setText(lostFindInfo.getAddress().toString());
        publisher.setText(lostFindInfo.getPublisher().toString());
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
