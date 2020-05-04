package com.frf.harbinmetro.messageboard;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageBoardActivity extends AppCompatActivity {

    private Toolbar message_board_activity_toolbar;//顶部导航栏
    private EditText message_board_activity_content;//详情输入框
    private Button message_board_activity_commit;//提交按钮
    private Button message_board_activity_look;//查看留言按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        message_board_activity_toolbar = findViewById(R.id.message_board_activity_toolbar);
        message_board_activity_toolbar.setTitle("乘客留言");
        message_board_activity_toolbar.setLogo(R.drawable.ic_action_message_board_white);
        setSupportActionBar(message_board_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        message_board_activity_content = findViewById(R.id.message_board_activity_edittext1);
        message_board_activity_commit = findViewById(R.id.message_board_activity_button);
        message_board_activity_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = message_board_activity_content.getText().toString();
                Toast.makeText(MessageBoardActivity.this,"你点击了提交按钮",Toast.LENGTH_SHORT).show();
                if(content.equals("")){
                    Toast.makeText(MessageBoardActivity.this,"提交留言不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                //向服务器发送提交留言请求
                commitExplainRequest(content,MyApplication.getPassenger().getId());
            }
        });

        message_board_activity_look = findViewById(R.id.message_board_activity_button2);
        message_board_activity_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageBoardActivity.this,"你点击了我的留言按钮",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessageBoardActivity.this, MyMessageActivity.class);
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

    /**
     * 向服务器发送提交留言请求
     * @param content
     * @param ofuserid
     */
    public void commitExplainRequest(final String content, final String ofuserid) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/AddExplainServlet";
        String tag = "AddExplain";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //解析从response中获得的json格式数据
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                //做自己的登录提交成功操作，如页面跳转
                                Toast.makeText(MessageBoardActivity.this, "提交成功！客服将会尽快回复您", Toast.LENGTH_SHORT).show();
                                message_board_activity_content.setText("");
                            } else {
                                //做自己的提交失败操作，如Toast提示
                                Toast.makeText(MessageBoardActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("TAG", error.getMessage(), error);
                    }
                })
                //是StringRequest匿名类的重写方法，设置请求参数
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content", content);
                params.put("ofuserid", ofuserid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
