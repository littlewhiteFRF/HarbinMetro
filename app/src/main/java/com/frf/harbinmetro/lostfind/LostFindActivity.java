package com.frf.harbinmetro.lostfind;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.database.DBUtil;
import com.frf.harbinmetro.database.HarbinMetroDB;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.lostfindinfo.LostFindInfoActivity;
import com.frf.harbinmetro.main.model.NewsInfo;
import com.frf.harbinmetro.notification.NotificationActivity;
import com.frf.harbinmetro.notification.model.NotificationInfo;
import com.frf.harbinmetro.util.Utils;
import com.frf.harbinmetro.web.WebActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.frf.harbinmetro.database.DBUtil.handleLostFindResponse;

public class LostFindActivity extends AppCompatActivity {

    private HarbinMetroDB harbinMetroDB;//数据库对象，内含各种数据库操作函数

    /**
     * 发送http请求后，成功返回数据的标识。 由接口定义，一般都是 0为正确
     */
    public final String RESULT_SUCCESS = "成功的返回";
    /**
     * 发送http请求后，返回次数限制的内容
     */
    public final String RESULT_ERROR = "超过每日可允许请求次数!";
    /**
     * API接口返回的json 格式的新闻数据
     */
    private String responseText;

    private Toolbar lostfind_activity_toolbar;//顶部导航栏
    private ListView lostfind_activity_listview;//失物招领列表
    private ArrayList<LostFindInfo> objects;//失物招领数据集合

    private BaseAdapter lostfindAdapter = new BaseAdapter() {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(LostFindActivity.this, R.layout.lostfind_activity_listview_items, null);
            } else {
                view = convertView;
            }

            ImageView imageView = view.findViewById(R.id.lostfind_activity_listview_item_imageview);
            TextView titleTextView = view.findViewById(R.id.lostfind_activity_listview_item_title);
            TextView featureTextView = view.findViewById(R.id.lostfind_activity_listview_item_feature);
            TextView dateTimeTextView = view.findViewById(R.id.lostfind_activity_listview_item_daytime);

            Glide.with(Utils.getContext()).load(objects.get(position).getImageurl().toString()).into(imageView);//一个图片加载框架
            titleTextView.setText(objects.get(position).getTitle());
            featureTextView.setText(objects.get(position).getFeature());
            dateTimeTextView.setText(objects.get(position).getDateTime());

            return view;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);

        //获得数据库对象
        harbinMetroDB =HarbinMetroDB.getInstance(this);

        lostfind_activity_toolbar = findViewById(R.id.lostfind_activity_toolbar);
        lostfind_activity_toolbar.setTitle("失物招领");
        lostfind_activity_toolbar.setLogo(R.drawable.ic_action_lost_find);
        setSupportActionBar(lostfind_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        objects = new ArrayList<LostFindInfo>();
//        for(int i=0;i<10;i++){
//            LostFindInfo lostFindInfo=new LostFindInfo();
//            lostFindInfo.setTitle("第"+i+"条失物招领");
//            lostFindInfo.setDateTime(new Date().toString());
//            objects.add(lostFindInfo);
//        }

        //objects = (ArrayList<LostFindInfo>) harbinMetroDB.loadLostFindInfo();

        //if(objects == null) {//如果从SQLite获得的数据为空，向服务器发送请求
            //通过okhttp请求数据
            getDataFromServer();//失物招领信息
        //}
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
     * 对返回的接送数据进行解析  并且返回一个集合，集合的内容是一个失物招领的详情信息
     *
     * @param result
     * @return 复习一下最原始的JSONObject解析方式
     * 使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
     */
    private ArrayList<LostFindInfo> parseData(String result) {
        try {
            //JSONObject jo1 = new JSONObject(result);
            //String joError = jo1.getString("reason");
            //if (joError.equals(RESULT_SUCCESS)) {
                // 接口成功返回数据
                //JSONObject jo2 = jo1.getJSONObject("result");
                //JSONArray ja = jo2.getJSONArray("data");
                JSONObject jo1 = new JSONObject(result);
                JSONArray ja = jo1.getJSONArray("params");
                ArrayList<LostFindInfo> InfoList = new ArrayList<LostFindInfo>();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    LostFindInfo info = new LostFindInfo();
                    info.setId(jo.getString("id"));
                    info.setTitle(jo.getString("title"));
                    info.setFeature(jo.getString("feature"));
                    info.setAddress(jo.getString("address"));
                    info.setDateTime(jo.getString("date"));
                    info.setPublisher(jo.getString("publisher"));
                    info.setState(jo.getString("state"));
                    info.setImageurl(jo.getString("imageurl"));
                    InfoList.add(info);
                }
                return InfoList;
            //} else {
                // 接口返回数据失败，可通过返回的code对照出具体原因，暂不处理
                //return null;
            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 核心功能
     * 发送http请求获取失物招领数据
     *
     * @param
     */
    public void getDataFromServer() {
        // 一个失物招领类型的访问请求。
        String newsUrl = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/Lf";
        Log.d("jiaBing", "请求的连接为" + newsUrl);
        //带着这个请求去访问。 就和你复制一个网址用电脑访问是一样的，比如复制下面的在浏览器上访问
        // http://v.juhe.cn/toutiao/index?type=toutiao&key=3dc86b09a2ee2477a5baa80ee70fcdf5
        Utils.sendOkHttpRequest(newsUrl, new Callback() {
            // 下面的onFailure 和 onResponse是请求的回调。 意思就是，如果失败了，就会执行onFailure，成功了就会执行onResponse
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("jiaBing", "请求失败" + e);
                //请求失败了，一般是没有网络的情况下
                //隐藏进度条，显示错误提示文本告诉用户这次请求失败
                //progressBar.setVisibility(View.GONE);
                //textView.setText(getString(R.string.result_error));
                Toast.makeText(LostFindActivity.this, "连接请求失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //请求成功
                responseText = response.body().string();
                //解析返回的json 格式的数据   这个parseData方法，丢进去了一个json格式的数据，他解析后悔返回一个list集合的新闻数据出来

                //parseData解析JSON数据
                objects = parseData(responseText);

                if (objects == null) {
                    //解析返回的json，结果是数据错误
                    // 因为android更新界面不能再子线程更新，要在主线程更新，
                    // 所以Utils.runOnUiThread这个工具类就是切换到主线程做更新UI界面的事
                    Utils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 三行代码，分别为， 把错误信息的文本显示出来
                            //把错误的信息设置到文本中
                            //把进度条隐藏起来
                            //textView.setVisibility(View.VISIBLE);
                            //textView.setText(getString(R.string.result_error));
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(LostFindActivity.this, "json解析错误！", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    //将从服务器获得的数据存储到本地SQLite的lostfindtable
                    DBUtil.handleLostFindResponse(harbinMetroDB, objects);
                    //请求成功，解析返回的json一切正常
                    Utils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //这里是recycleView 的一些基本操作。 想了解的话百度一下，有点印象也可以。看不到也没关系， 就是显示新闻列表的控件
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            NewsInfoAdapter newsInfoAdapter = new NewsInfoAdapter(mInfoList);
//                            recyclerView.setAdapter(newsInfoAdapter);
//                            //一切正常，拿到数据后显示数据，同时也隐藏进度条
//                            recyclerView.setVisibility(View.VISIBLE);
//                            progressBar.setVisibility(View.GONE);
                            lostfind_activity_listview = LostFindActivity.this.findViewById(R.id.lostfind_activity_listview);
                            lostfind_activity_listview.setAdapter(lostfindAdapter);
                            lostfind_activity_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    // TODO Auto-generated method stub

                                    Toast.makeText(LostFindActivity.this,"你点击了第"+(arg2+1)+"条失物招领",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LostFindActivity.this, LostFindInfoActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("onelostfind", objects.get(arg2));
                                    intent.putExtra("bundle",bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
