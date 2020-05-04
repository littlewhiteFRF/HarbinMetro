package com.frf.harbinmetro.register;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.frf.harbinmetro.myinfo.MyInfoActivity;
import com.frf.harbinmetro.register.model.User;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_activity_et_username;//用户名编辑框
    private EditText register_activity_et_password;//密码编辑框
    private EditText register_activity_et_repeatpassword;//确认密码编辑框

    private Button register_activity_bt_go;//注册按钮

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_activity_et_username = findViewById(R.id.register_activity_et_username);
        register_activity_et_password = findViewById(R.id.register_activity_et_password);
        register_activity_et_repeatpassword = findViewById(R.id.register_activity_et_repeatpassword);

        register_activity_bt_go = (Button)findViewById(R.id.register_activity_bt_go);
        register_activity_bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this,"你点击了注册按钮",Toast.LENGTH_SHORT).show();
                if(register_activity_et_username.getText().toString().equals("")||register_activity_et_password.getText().toString().equals("")||register_activity_et_repeatpassword.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"注册信息不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    //如果密码和确认密码相同才允许向服务器发送请求
                    if (register_activity_et_password.getText().toString().equals(register_activity_et_repeatpassword.getText().toString())) {
                        //向服务器发送注册请求
                        RegisterRequest(register_activity_et_username.getText().toString(), register_activity_et_password.getText().toString());
                    } else {//如果密码和确认密码不同，则吐司提示
                        Toast.makeText(RegisterActivity.this, "密码和确认密码不同", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    /**
     * 向服务器发送注册请求
     * @param username
     * @param password
     */
    public void RegisterRequest(final String username, final String password) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/RegisterServlet";
        String tag = "Register";

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
                                //获得新注册用户信息
                                user = new User();
                                user.setId(jsonObject.getString("userid"));
                                user.setName(jsonObject.getString("username"));
                                user.setPassword(jsonObject.getString("password"));
                                user.setType(jsonObject.getString("type"));
                                //注册成功后，结束注册页面
                                finish();
                                //成功后，跳转到个人信息完善页面
                                Intent intent = new Intent(RegisterActivity.this, SetInfoActivity.class);
                                intent.putExtra("newuser",user);
                                startActivity(intent);
                            }
                            if(result.equals("double")){
                                //做自己的注册失败操作，如Toast提示
                                Toast.makeText(RegisterActivity.this, "用户名重名!", Toast.LENGTH_SHORT).show();
                            }
                            if(result.equals("failed")){
                                //做自己的注册失败操作，如Toast提示
                                Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
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
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
