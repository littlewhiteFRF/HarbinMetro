package com.frf.harbinmetro.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.register.RegisterActivity;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;//用户名
    private EditText et_password;//密码
    private Button btn_login;//登陆按钮
    private Button btn_register;//注册按钮

    private Passenger passenger;//乘客对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.login_activity_et_username);
        et_password = findViewById(R.id.login_activity_et_password);
        btn_login = findViewById(R.id.login_activity_bt_go);
        btn_register = findViewById(R.id.login_activity_bt_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this,"你点击了登陆按钮",Toast.LENGTH_SHORT).show();
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(username.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名或者密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //发送登陆请求
                    LoginRequest(username,password);
                }


            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"你点击了注册按钮",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 发送登陆请求
     * @param accountNumber
     * @param password
     */
    public void LoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/Login";
        String tag = "Login";

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
                                //修改登陆状态loginState为true
                                MyApplication.setLoginState(true);
                                //登陆验证成功后，继续解析jsonObject对象，获得该用户的所有信息
                                passenger = new Passenger();
                                passenger.setId(jsonObject.getString("id"));
                                passenger.setUsername(jsonObject.getString("username"));
                                passenger.setPassword(jsonObject.getString("password"));
                                passenger.setType(jsonObject.getString("type"));
                                passenger.setIdcard(jsonObject.getString("idcard"));
                                passenger.setRealname(jsonObject.getString("realname"));
                                passenger.setSex(jsonObject.getString("sex"));
                                passenger.setPhonenumber(jsonObject.getString("phonenumber"));
                                passenger.setBirthday(jsonObject.getString("birthday"));
                                passenger.setEmail(jsonObject.getString("email"));
                                passenger.setAlipayid(jsonObject.getString("alipayid"));
                                passenger.setWechatid(jsonObject.getString("wechatid"));
                                passenger.setBankid(jsonObject.getString("bankid"));
                                passenger.setImageurl(jsonObject.getString("imageurl"));
                                passenger.setAlipaypassword(jsonObject.getString("alipaypassword"));
                                passenger.setMoney(jsonObject.getString("money"));
                                MyApplication.setPassenger(passenger);
                                //利用偏好存储对已经登陆的乘客信息进行存储，下次再进入APP，保证不用再登陆
                                savePassenger(passenger);
                                //做自己的登录成功操作，如页面跳转
                                Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                //登陆成功后，结束登陆页面
                                finish();
                                //登陆成功后，跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
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
                params.put("name", accountNumber);
                params.put("password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    /**
     * 利用SharedPreferences(偏好存储)，对已登陆的乘客对象进行保存，保证APP退出时，保留该乘客的登陆信息
     * @param passenger
     */
    public void savePassenger(Passenger passenger){
        SharedPreferences preferences = getSharedPreferences("passenger",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id",passenger.getId());
        editor.putString("username",passenger.getUsername());
        editor.putString("password",passenger.getPassword());
        editor.putString("type",passenger.getType());
        editor.putString("idcard",passenger.getIdcard());
        editor.putString("realname",passenger.getRealname());
        editor.putString("sex",passenger.getSex());
        editor.putString("phonenumber",passenger.getPhonenumber());
        editor.putString("birthday",passenger.getBirthday());
        editor.putString("email",passenger.getEmail());
        editor.putString("alipayid",passenger.getAlipayid());
        editor.putString("wechatid",passenger.getWechatid());
        editor.putString("bankid",passenger.getBankid());
        editor.putString("imageurl",passenger.getImageurl());
        editor.putString("alipaypassword",passenger.getAlipaypassword());
        editor.putString("money",passenger.getMoney());

        editor.commit();
    }
}
