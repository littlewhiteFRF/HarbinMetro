package com.frf.harbinmetro.myinfo;

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

public class UpdateUserNameActivity extends AppCompatActivity {

    private Toolbar update_username_activity_toolbar;//顶部导航栏

    private EditText et_username;//修改用户名编辑框
    private Button btn_update;//修改按钮

    private Passenger passenger;//乘客对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_name);

        update_username_activity_toolbar = findViewById(R.id.update_username_activity_toolbar);
        update_username_activity_toolbar.setTitle("修改用户名");
        update_username_activity_toolbar.setLogo(R.drawable.ic_action_myinfo_white);
        setSupportActionBar(update_username_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        et_username = findViewById(R.id.update_username_activity_edittext);
        btn_update = findViewById(R.id.update_username_activity_button);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateUserNameActivity.this, "你点击了修改按钮", Toast.LENGTH_SHORT).show();
                String update_username = et_username.getText().toString();
                if(update_username.equals("")){
                    Toast.makeText(UpdateUserNameActivity.this, "修改用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(update_username.equals(MyApplication.getPassenger().getUsername())){
                    Toast.makeText(UpdateUserNameActivity.this, "修改用户名不能与之前相同", Toast.LENGTH_SHORT).show();
                    return;
                }

                //发送修改用户名请求
                UpdateUsernameRequest(MyApplication.getPassenger().getId(),update_username);
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
     * 向服务器发送修改用户名请求
     * @param userid
     * @param update_username
     */
    public void UpdateUsernameRequest(final String userid, final String update_username) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/UpdateUsernameServlet";
        String tag = "UpdateUsername";

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
                            if (result.equals("success")) {//修改成功
                                //修改成功后，继续解析jsonObject对象，对原来的乘客信息进行修改
                                MyApplication.getPassenger().setUsername(update_username);
                                //做自己的修改成功操作，如Toast提示和页面跳转
                                Toast.makeText(UpdateUserNameActivity.this, "修改用户名成功！", Toast.LENGTH_SHORT).show();
                                //修改成功后，结束修改页面
                                finish();
                                //修改成功后，跳转到个人信息页面
                                Intent intent = new Intent(UpdateUserNameActivity.this, MyInfoActivity.class);
                                startActivity(intent);
                            }
                            if(result.equals("failed")){//修改失败
                                //做自己的修改失败操作，如Toast提示
                                Toast.makeText(UpdateUserNameActivity.this, "修改用户名失败！", Toast.LENGTH_SHORT).show();
                            }
                            if(result.equals("double")){//用户名已存在
                                Toast.makeText(UpdateUserNameActivity.this, "修改用户名已存在！", Toast.LENGTH_SHORT).show();
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
                params.put("userid", userid);
                params.put("updateusername", update_username);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
