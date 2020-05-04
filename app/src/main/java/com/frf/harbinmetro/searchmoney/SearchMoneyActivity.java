package com.frf.harbinmetro.searchmoney;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.addmoney.AddMoneyActivity;
import com.frf.harbinmetro.lineinfo.LineInfoActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;
import com.frf.harbinmetro.searchline.model.Line;
import com.frf.harbinmetro.util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchMoneyActivity extends AppCompatActivity {

    private Toolbar search_money_activity_toolbar;//顶部导航栏

    private TextView money;

    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_money);

        search_money_activity_toolbar = findViewById(R.id.search_money_activity_toolbar);
        search_money_activity_toolbar.setTitle("余额查询");
        search_money_activity_toolbar.setLogo(R.drawable.ic_action_money_search);
        setSupportActionBar(search_money_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        money = findViewById(R.id.search_money_activity_money);

//        if(Integer.parseInt(MyApplication.getPassenger().getMoney())<=10){//如果余额小于等于10元，字体变红色
//            money.setTextColor(ColorStateList.valueOf(0xFFFD081C));
//        }
//        money.setText("¥"+MyApplication.getPassenger().getMoney()+".00");

        //向服务器发送查询余额请求
        searchMoneyRequest(MyApplication.getPassenger().getAlipayid());

        btn_add = findViewById(R.id.search_money_activity_button);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchMoneyActivity.this,"你点击了充值按钮",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchMoneyActivity.this, AddMoneyActivity.class);
                startActivity(intent);
                //结束当前查询余额页面
                finish();
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
     * 向服务器发送余额查询请求
     * @param payid
     */
    public void searchMoneyRequest(final String payid) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/SearchMoneyServlet";
        String tag = "SearchMoney";

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
                                //查找验证成功后，继续解析jsonObject对象，获得余额信息

                                //做自己的查询成功操作，如页面跳转
                                Toast.makeText(SearchMoneyActivity.this, "查询余额成功！", Toast.LENGTH_SHORT).show();
                                //查询成功后，并更新本地账户数据
                                MyApplication.getPassenger().setMoney(jsonObject.getString("money"));

                                if(Integer.parseInt(MyApplication.getPassenger().getMoney())<=10){//如果余额小于等于10元，字体变红色
                                    money.setTextColor(ColorStateList.valueOf(0xFFFD081C));
                                }
                                money.setText("¥"+MyApplication.getPassenger().getMoney()+".00");

                            } else {
                                //做自己的查询失败操作，如Toast提示
                                Toast.makeText(SearchMoneyActivity.this, "查询余额失败！", Toast.LENGTH_SHORT).show();
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
                params.put("payid", payid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
