package com.frf.harbinmetro.searchline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.lineinfo.LineInfoActivity;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.searchline.model.Line;
import com.frf.harbinmetro.searchstation.SearchStationActivity;
import com.frf.harbinmetro.searchstation.model.Station;
import com.frf.harbinmetro.stationinfo.StationInfoActivity;
import com.frf.harbinmetro.web.WebActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchLineActivity extends AppCompatActivity {
    public static final int SELECT_LINE1 = 0;//一号线换三号线
    public static final int SELECT_LINE3 = 1;//三号线换一号线
    private Toolbar search_line_activity_toolbar;//顶部导航栏
    private Button search_line_activity_button;//确认支付按钮
    private Spinner search_line_activity_start_line_spinner;//起点线路下拉框
    private Spinner search_line_activity_finish_line_spinner;//终点线路下拉框
    private Spinner search_line_activity_start_spinner;//起点下拉框
    private Spinner search_line_activity_finish_spinner;//终点下拉框
    private String[] line;//线路
    private String[] line1Station;//一号线站点
    private String[] line3Station;//三号线站点
    private ArrayAdapter<String> lineAdapter;//创建一个数组适配器（线路选择）
    private ArrayAdapter<String> line1Adapter;//创建一个数组适配器（一号线）
    private ArrayAdapter<String> line3Adapter;//创建一个数组适配器（三号线）

    private static String startStation;//起始站点名称
    private static String endStation;//到达站点名称

    private Line lineInfo;//线路对象

    //异步消息处理机制，切换起点线路，对应站点进行更换
    private Handler startLineChangeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_LINE1://选择地铁一号线
                    //起点选择器
                    search_line_activity_start_spinner.setAdapter(line1Adapter);
                    search_line_activity_start_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            startStation = line1Adapter.getItem(position).toString().trim();
                            //Toast.makeText(SaleTicketActivity.this,startStation,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                case SELECT_LINE3://选择地铁三号线
                    //起点选择器
                    search_line_activity_start_spinner.setAdapter(line3Adapter);
                    search_line_activity_start_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            startStation = line3Adapter.getItem(position).toString().trim();
                            //Toast.makeText(SaleTicketActivity.this,startStation,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                default:
                    break;
            }
        }

    };

    //异步消息处理机制，切换终点线路，对应站点进行更换
    private Handler finishLineChangeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_LINE1://选择地铁一号线
                    //终点选择器
                    search_line_activity_finish_spinner.setAdapter(line1Adapter);
                    search_line_activity_finish_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            endStation = line1Adapter.getItem(position).toString().trim();
                            //Toast.makeText(SaleTicketActivity.this,endStation,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                case SELECT_LINE3://选择地铁三号线
                    //终点选择器
                    search_line_activity_finish_spinner.setAdapter(line3Adapter);
                    search_line_activity_finish_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            endStation = line3Adapter.getItem(position).toString().trim();
                            //Toast.makeText(SaleTicketActivity.this,endStation,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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
        setContentView(R.layout.activity_search_line);

        search_line_activity_toolbar = findViewById(R.id.search_line_activity_toolbar);
        search_line_activity_toolbar.setTitle("线路查询");
        search_line_activity_toolbar.setLogo(R.drawable.ic_action_search_line);
        setSupportActionBar(search_line_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        //线路信息
        line = new String[]{"一号线", "三号线"};

        //线路站点信息
        line1Station = new String[]{"哈东站", "桦树街站", "交通学院站", "太平桥站", "工程大学站", "烟厂站", "医大一院站", "博物馆站", "铁路局站", "哈工大站", "西大桥站", "和兴路站", "学府路站", "理工大学站", "黑龙江大学站", "医大二院站", "哈达站", "哈尔滨南站"};
        line3Station = new String[]{"医大二院站", "哈西大街站", "哈尔滨大街站", "哈尔滨西站"};

        //创建适配器
        lineAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, line);  //创建一个数组适配器
        line1Adapter = new ArrayAdapter<String>(SearchLineActivity.this, android.R.layout.simple_spinner_item, line1Station);  //创建一个数组适配器（一号线）
        line3Adapter = new ArrayAdapter<String>(SearchLineActivity.this, android.R.layout.simple_spinner_item, line3Station);  //创建一个数组适配器（三号线）

        //设置下拉列表框的下拉选项样式
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        line1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        line3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //起点线路选择器
        search_line_activity_start_line_spinner = findViewById(R.id.search_line_activity_start_line_spinner);
        search_line_activity_start_line_spinner.setAdapter(lineAdapter);

        //终点线路选择器
        search_line_activity_finish_line_spinner = findViewById(R.id.search_line_activity_finish_line_spinner);
        search_line_activity_finish_line_spinner.setAdapter(lineAdapter);

        //起点选择器
        search_line_activity_start_spinner = findViewById(R.id.search_line_activity_start_station_spinner);
        search_line_activity_start_spinner.setAdapter(line1Adapter);

        //终点选择器
        search_line_activity_finish_spinner = findViewById(R.id.search_line_activity_finish_station_spinner);
        search_line_activity_finish_spinner.setAdapter(line1Adapter);

        //选择item的选择点击监听事件(选择起点线路)
        search_line_activity_start_line_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + arg2+"个");//文本说明
                if (arg2 == 0) {//选择地铁一号线
                    //新开一个线程
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = SELECT_LINE1;
                            startLineChangeHandler.sendMessage(message);
                        }
                    }).start();
                } else {//选择地铁三号线
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = SELECT_LINE3;
                            startLineChangeHandler.sendMessage(message);
                        }
                    }).start();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });

        //选择item的选择点击监听事件(选择起点线路)
        search_line_activity_finish_line_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + arg2+"个");//文本说明
                if (arg2 == 0) {//选择地铁一号线
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = SELECT_LINE1;
                            finishLineChangeHandler.sendMessage(message);
                        }
                    }).start();
                } else {//选择地铁三号线
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = SELECT_LINE3;
                            finishLineChangeHandler.sendMessage(message);
                        }
                    }).start();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });

        //监听查询按钮事件
        search_line_activity_button = findViewById(R.id.search_line_activity_button);
        search_line_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startStation.equals(endStation)) {//如果起点和终点相同，要有警告提示
                    tipsDiaog();
                } else {//如果起点和终点不相同，跳转查询页面
                    Toast.makeText(SearchLineActivity.this, "你点击了查询线路按钮", Toast.LENGTH_SHORT).show();
                    //向服务器发送查询线路请求
                    searchLineRequest(startStation, endStation);
//                    http://mditie.mapbar.com/haerbin/transfer?org=?&dst=?
//                    Intent intent = new Intent(SearchLineActivity.this, LineInfoActivity.class);
//                    intent.putExtra("url", "http://mditie.mapbar.com/haerbin/transfer?org="+startStation+"&dst="+endStation);
//                    startActivity(intent);
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

    public void tipsDiaog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchLineActivity.this);
        builder.setIcon(R.drawable.ic_action_tips);//设置小图标
        builder.setTitle("温馨提示");//设置标题
        builder.setMessage("起点和终点不能相同！");//设置内容

        //添加监听事件
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //设置对话框按钮
        builder.show();
    }

    /**
     * 向服务器发送查询站点请求
     *
     * @param startstation
     * @param finishstation
     */
    public void searchLineRequest(final String startstation, final String finishstation) {
        //请求地址
        String url = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/SearchLineServlet";
        String tag = "SearchLine";

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
                                lineInfo = new Line();
                                lineInfo.setLineid(jsonObject.getString("lineid"));
                                lineInfo.setStartstation(jsonObject.getString("startstation"));
                                lineInfo.setFinishstation(jsonObject.getString("finishstation"));
                                lineInfo.setTicketprice(jsonObject.getString("ticketprice"));
                                lineInfo.setPassstation(jsonObject.getString("passstation"));
                                lineInfo.setLineurl(jsonObject.getString("lineurl"));
                                //做自己的查询成功操作，如页面跳转
                                Toast.makeText(SearchLineActivity.this, "查询线路成功！", Toast.LENGTH_SHORT).show();
                                //查询成功后，跳转到站点详情页面
                                Intent intent = new Intent(SearchLineActivity.this, LineInfoActivity.class);
                                intent.putExtra("url", lineInfo.getLineurl());
                                startActivity(intent);
                            } else {
                                //做自己的查询失败操作，如Toast提示
                                Toast.makeText(SearchLineActivity.this, "查询线路失败！", Toast.LENGTH_SHORT).show();
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
                params.put("startstation", startstation);
                params.put("finishstation", finishstation);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }


}
