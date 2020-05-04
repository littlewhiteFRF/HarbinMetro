package com.frf.harbinmetro.register;

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
import android.widget.RadioButton;
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
import com.frf.harbinmetro.register.model.User;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetInfoActivity extends AppCompatActivity {

    private Toolbar set_info_activity_toolbar;//顶部导航栏

    private User user;
    private Passenger passenger;

    private String name;
    private String idcard;
    private String sex;
    private String phone;
    private String birthday;
    private String email;

    private EditText et_name;
    private EditText et_idcard;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private EditText et_phone;
    private EditText et_birthday;
    private EditText et_email;

    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);

        set_info_activity_toolbar = findViewById(R.id.set_info_activity_toolbar);
        set_info_activity_toolbar.setTitle("完善个人信息");
        set_info_activity_toolbar.setLogo(R.drawable.ic_action_myinfo_white);
        setSupportActionBar(set_info_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        et_name = findViewById(R.id.set_info_activity_edittext_name);
        et_idcard = findViewById(R.id.set_info_activity_edittext_idcare);
        rb_man = findViewById(R.id.set_info_activity_radiobutton1);
        rb_woman = findViewById(R.id.set_info_activity_radiobutton2);
        et_phone = findViewById(R.id.set_info_activity_edittext_phone);
        et_birthday = findViewById(R.id.set_info_activity_edittext_birthday);
        et_email = findViewById(R.id.set_info_activity_edittext_email);

        //获得由上一个活动传递过来的新User对象
        user = new User();
        user = (User) getIntent().getSerializableExtra("newuser");

        btn_sure = findViewById(R.id.set_info_activity_btn);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                idcard = et_idcard.getText().toString();
                if(rb_man.isChecked()){
                    sex = "男";
                }
                if(rb_woman.isChecked()){
                    sex = "女";
                }
                phone = et_phone.getText().toString();
                birthday = et_birthday.getText().toString();
                email = et_email.getText().toString();
                Toast.makeText(SetInfoActivity.this,"你点击了提交按钮",Toast.LENGTH_SHORT).show();
                if(name.equals("")||idcard.equals("")){
                    Toast.makeText(SetInfoActivity.this,"有必填项未填写",Toast.LENGTH_SHORT).show();
                }else{
                    //向服务器发送提交个人信息请求
                    SetInfoRequest(user.getId(),name,idcard,sex,phone,birthday,email);
                }
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
     * 向服务器发送提交个人信息请求
     * @param userid
     * @param name
     * @param idcard
     * @param sex
     * @param phone
     * @param birthday
     * @param email
     */
    public void SetInfoRequest(final String userid, final String name, final String idcard, final String sex, final String phone, final String birthday, final String email) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/SetInfoServlet";
        String tag = "SetInfo";

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
                                //做自己的成功提交功操作，如页面跳转
                                Toast.makeText(SetInfoActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                //提交成功后，结束完善信息页面
                                finish();
                                //提交成功后，跳转到主页面
                                Intent intent = new Intent(SetInfoActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //做自己的提交失败操作，如Toast提示
                                Toast.makeText(SetInfoActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
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
                params.put("name", name);
                params.put("idcard", idcard);
                params.put("sex", sex);
                params.put("phone", phone);
                params.put("birthday", birthday);
                params.put("email", email);

                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
