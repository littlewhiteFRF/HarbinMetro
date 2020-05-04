package com.frf.harbinmetro.searchstation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.login.model.Passenger;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.searchstation.model.Station;
import com.frf.harbinmetro.stationinfo.StationInfoActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.web.WebActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SearchStationActivity extends AppCompatActivity {

    public static final int UDATE_LINE1_TO_LINE3 = 1;//一号线换三号线
    public static final int UDATE_LINE3_TO_LINE1 = 2;//三号线换一号线
    private Toolbar search_station_activity_toolbar;//顶部导航栏
    private CardView cardView_line1;//地铁一号线卡片
    private CardView cardView_line3;//地铁三号线卡片
    private List<String> objects;///存储站点
    private ListView stationListview;//站点列表
    //    private List<String> stationUrl;//站点详细信息Url
    private Station station;//站点对象
    private BaseAdapter stationAdapter = new BaseAdapter() {//站点列表适配器

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//获得适配器的内容
            View view;
            if (convertView == null) {
                view = View.inflate(SearchStationActivity.this, R.layout.search_station_activity_items, null);
            } else {
                view = convertView;
            }

            TextView stationTextView = view.findViewById(R.id.search_station_activity_item_textview);

            stationTextView.setText(objects.get(position));

            return view;
        }
    };//站点列表的适配器
    //异步消息处理机制，切换线路，对应站点进行更换
    private Handler lineChangeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UDATE_LINE1_TO_LINE3://点击三号线卡片
                    stationListview.setAdapter(stationAdapter);
                    cardView_line3.setCardBackgroundColor(getResources().getColor(R.color.lineYesSelect));
                    cardView_line1.setCardBackgroundColor(getResources().getColor(R.color.lineNoSelect));

                    stationListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            // TODO Auto-generated method stub

                            Toast.makeText(SearchStationActivity.this, "你选择了" + objects.get(arg2).toString() + "站点", Toast.LENGTH_SHORT).show();
                            searchStationRequest(objects.get(arg2).toString());
//                            Intent intent = new Intent(SearchStationActivity.this, StationInfoActivity.class);
//                            intent.putExtra("url", stationUrl.get(arg2+18).toString());
//                            startActivity(intent);
                        }
                    });
                    break;
                case UDATE_LINE3_TO_LINE1://点击一号线卡片
                    stationListview.setAdapter(stationAdapter);
                    cardView_line1.setCardBackgroundColor(getResources().getColor(R.color.lineYesSelect));
                    cardView_line3.setCardBackgroundColor(getResources().getColor(R.color.lineNoSelect));

                    stationListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            // TODO Auto-generated method stub

                            Toast.makeText(SearchStationActivity.this, "你选择了" + objects.get(arg2).toString() + "站点", Toast.LENGTH_SHORT).show();
                            searchStationRequest(objects.get(arg2).toString());
//                            Intent intent = new Intent(SearchStationActivity.this, StationInfoActivity.class);
//                            intent.putExtra("url", stationUrl.get(arg2).toString());
//                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        search_station_activity_toolbar = findViewById(R.id.search_station_activity_toolbar);
        search_station_activity_toolbar.setTitle("站点查询");
        search_station_activity_toolbar.setLogo(R.drawable.ic_action_search_station);
        setSupportActionBar(search_station_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        cardView_line1 = findViewById(R.id.search_station_activity_cardview1);//地铁一号线卡片
        cardView_line3 = findViewById(R.id.search_station_activity_cardview2);//地铁三号线卡片
        //一开始需要初始化站点列表，默认显示地铁一号线站点
        objects = new ArrayList<String>();
        objects.add("哈尔滨东站");
        objects.add("桦树街站");
        objects.add("交通学院站");
        objects.add("太平桥站");
        objects.add("工程大学站");
        objects.add("烟厂站");
        objects.add("医大一院站");
        objects.add("博物馆站");
        objects.add("铁路局站");
        objects.add("哈工大站");
        objects.add("西大桥站");
        objects.add("和兴路站");
        objects.add("学府路站");
        objects.add("理工大学站");
        objects.add("黑龙江大学站");
        objects.add("医大二院站");
        objects.add("哈达站");
        objects.add("哈尔滨南站");
        cardView_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.clear();
                objects.add("哈尔滨东站");
                objects.add("桦树街站");
                objects.add("交通学院站");
                objects.add("太平桥站");
                objects.add("工程大学站");
                objects.add("烟厂站");
                objects.add("医大一院站");
                objects.add("博物馆站");
                objects.add("铁路局站");
                objects.add("哈工大站");
                objects.add("西大桥站");
                objects.add("和兴路站");
                objects.add("学府路站");
                objects.add("理工大学站");
                objects.add("黑龙江大学站");
                objects.add("医大二院站");
                objects.add("哈达站");
                objects.add("哈尔滨南站");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UDATE_LINE3_TO_LINE1;
                        lineChangeHandler.sendMessage(message);
                    }
                }).start();
            }
        });
        cardView_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.clear();
                objects.add("医大二院站");
                objects.add("哈西大街站");
                objects.add("哈尔滨大街站");
                objects.add("哈尔滨西站");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UDATE_LINE1_TO_LINE3;
                        lineChangeHandler.sendMessage(message);
                    }
                }).start();
            }
        });

//        stationUrl = new ArrayList<String>();
//        stationUrl.add("https://m.huoche.net/haerbinstation/2315/");//0哈东站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2316/");//1桦树街站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1403/");//2交通学院站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1404/");//3太平桥站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1405/");//4工程大学站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1406/");//5烟厂站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2317/");//6医大一院站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1408/");//7博物馆站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1409/");//8铁路局站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2318/");//9哈工大站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1412/");//10西大桥站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2319/");//11和兴路站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1420/");//12学府路站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1415/");//13理工大学站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1416/");//14黑龙江大学站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2320/");//15医大二院站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2321/");//16哈达站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2322/");//17哈尔滨南站
//        stationUrl.add("https://m.huoche.net/haerbinstation/2320/");//18医大二院站
//        stationUrl.add("https://m.huoche.net/haerbinstation/5772/");//19哈西大街站
//        stationUrl.add("https://m.huoche.net/haerbinstation/5773/");//20哈尔滨大街站
//        stationUrl.add("https://m.huoche.net/haerbinstation/1466/");//21哈尔滨西站

        //站点列表适配
        stationListview = findViewById(R.id.search_station_activity_listview);
        stationListview.setAdapter(stationAdapter);
        stationListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                Toast.makeText(SearchStationActivity.this, "你选择了" + objects.get(arg2).toString() + "站点", Toast.LENGTH_SHORT).show();
                searchStationRequest(objects.get(arg2).toString());
//                Intent intent = new Intent(SearchStationActivity.this, StationInfoActivity.class);
//                intent.putExtra("url", stationUrl.get(arg2).toString());
//                startActivity(intent);
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
     * 查询站点信息
     * @param stationname
     */
    public void searchStationRequest(final String stationname) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/SearchStationServlet";
        String tag = "SearchStation";

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
                                //查找验证成功后，继续解析jsonObject对象，获得该站点的所有信息
                                station= new Station();
                                station.setStationid(jsonObject.getString("stationid"));
                                station.setStationname(jsonObject.getString("stationname"));
                                station.setStationurl(jsonObject.getString("stationurl"));
                                //做自己的查询成功操作，如页面跳转
                                Toast.makeText(SearchStationActivity.this, "查询站点成功！", Toast.LENGTH_SHORT).show();
                                //查询成功后，跳转到站点详情页面
                                Intent intent = new Intent(SearchStationActivity.this, StationInfoActivity.class);
                                intent.putExtra("url", station.getStationurl());
                                startActivity(intent);
                            } else {
                                //做自己的查询失败操作，如Toast提示
                                Toast.makeText(SearchStationActivity.this, "查询站点失败！", Toast.LENGTH_SHORT).show();
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
                params.put("stationname",stationname);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
