package com.frf.harbinmetro.messageboard;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.goods.model.Goods;
import com.frf.harbinmetro.messageboard.model.Explain;

public class MessageInfoActivity extends AppCompatActivity {

    private Toolbar message_info_activity_toolbar;//顶部导航栏
    private Explain explain;
    private TextView content;
    private TextView answer;
    private TextView datetime;
    private Button btn_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);

        message_info_activity_toolbar = findViewById(R.id.message_info_activity_toolbar);
        message_info_activity_toolbar.setTitle("留言详情");
        message_info_activity_toolbar.setLogo(R.drawable.ic_action_message_board_white);
        setSupportActionBar(message_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        content = findViewById(R.id.message_info_activity_content);
        answer = findViewById(R.id.message_info_activity_answer);
        datetime = findViewById(R.id.message_info_activity_datetime);

        //获得可用订单信息
        explain = new Explain();
        explain = (Explain) getIntent().getSerializableExtra("messageinfo");

        content.setText(explain.getContent());
        answer.setText(explain.getAnswer());
        datetime.setText(explain.getDatetime());

        btn_contact = findViewById(R.id.message_info_activity_btn_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//拨打电话（跳转到拨号界面，用户手动点击拨打）
                Toast.makeText(MessageInfoActivity.this,"你点击了联系客服",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "18846074770");
                intent.setData(data);
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
