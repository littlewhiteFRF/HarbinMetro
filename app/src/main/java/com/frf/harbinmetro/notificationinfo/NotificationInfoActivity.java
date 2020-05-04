package com.frf.harbinmetro.notificationinfo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.notification.model.NotificationInfo;

public class NotificationInfoActivity extends AppCompatActivity {

    private Toolbar notification_info_activity_toolbar;//顶部导航栏

    private TextView title;//标题
    private TextView date;//日期
    private TextView content;//内容
    private TextView publisher;//发布者

    private NotificationInfo notificationInfo;//通告通知信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_info);

        notification_info_activity_toolbar = findViewById(R.id.notification_info_activity_toolbar);
        notification_info_activity_toolbar.setTitle("通告通知详情");
        notification_info_activity_toolbar.setLogo(R.drawable.ic_action_lost_find);
        setSupportActionBar(notification_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        title = findViewById(R.id.notification_info_activity_title);
        date = findViewById(R.id.notification_info_activity_date);
        content = findViewById(R.id.notification_info_activity_content);
        publisher = findViewById(R.id.notification_info_activity_publisher);

        Bundle bundle = (Bundle) getIntent().getExtras().get("bundle");
        notificationInfo = new NotificationInfo();
        notificationInfo = (NotificationInfo) bundle.getParcelable("onenotification");

        title.setText(notificationInfo.getTitle().toString());
        date.setText(notificationInfo.getDateTime().toString());
        content.setText(notificationInfo.getContent().toString());
        publisher.setText(notificationInfo.getPublisher().toString());

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
