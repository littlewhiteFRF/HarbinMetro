package com.frf.harbinmetro.bindaccount;

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
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.register.SetInfoActivity;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BindAccountActivity extends AppCompatActivity {

    private Toolbar bind_account_activity_toolbar;//顶部导航栏

    private EditText account;
    private EditText password;

    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        bind_account_activity_toolbar = findViewById(R.id.bind_account_activity_toolbar);
        bind_account_activity_toolbar.setTitle("绑定支付账户");
        bind_account_activity_toolbar.setLogo(R.drawable.ic_action_bind_pay_white);
        setSupportActionBar(bind_account_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        account = findViewById(R.id.bind_account_activity_edittext_alipayid);
        password = findViewById(R.id.bind_account_activity_edittext_password);

        btn_sure = findViewById(R.id.bind_account_activity_btn);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString().equals("")||password.getText().toString().equals("")){
                    Toast.makeText(BindAccountActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    //向服务器发送绑定支付账户请求
                    SetInfoRequest(account.getText().toString(),password.getText().toString());
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

    public void SetInfoRequest(final String account, final String password) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/BindAccountServlet";
        String tag = "BindAccount";

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
                                //完善本地乘客信息
                                MyApplication.getPassenger().setAlipayid(jsonObject.getString("account"));
                                MyApplication.getPassenger().setAlipaypassword(jsonObject.getString("password"));
                                MyApplication.getPassenger().setMoney(jsonObject.getString("money"));
                                //做自己的成功提交功操作，如页面跳转
                                Toast.makeText(BindAccountActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                //提交成功后，结束完善信息页面
                                finish();
                                //提交成功后，跳转到主页面
                                Intent intent = new Intent(BindAccountActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //做自己的提交失败操作，如Toast提示
                                Toast.makeText(BindAccountActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
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
                params.put("userid",MyApplication.getPassenger().getId());
                params.put("account", account);
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
