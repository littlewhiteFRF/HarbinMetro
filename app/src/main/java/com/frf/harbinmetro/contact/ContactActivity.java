package com.frf.harbinmetro.contact;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.contact.model.QuestionInfo;
import com.frf.harbinmetro.database.DBUtil;
import com.frf.harbinmetro.database.HarbinMetroDB;
import com.frf.harbinmetro.lostfind.LostFindActivity;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.lostfindinfo.LostFindInfoActivity;
import com.frf.harbinmetro.messageboard.MessageInfoActivity;
import com.frf.harbinmetro.notification.NotificationActivity;
import com.frf.harbinmetro.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContactActivity extends AppCompatActivity {
    private HarbinMetroDB harbinMetroDB;//数据库对象，内含各种数据库操作函数

    private Toolbar contact_activity_toolbar;//顶部导航栏
    private ListView contact_activity_listview;//热心帮助列表
    private ArrayList<QuestionInfo> objects;//问题数据组合
    private Button contact_activity_button;//联系按钮

    private String responseText;//响应体

    private BaseAdapter questionAdapter = new BaseAdapter() {
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
                view = View.inflate(ContactActivity.this, R.layout.contact_activity_listview_items, null);
            } else {
                view = convertView;
            }

            ImageView questionImageView = view.findViewById(R.id.contact_activity_listview_items_imageview);
            TextView titleTextView = view.findViewById(R.id.contact_activity_listview_items_textview);

            questionImageView.setImageDrawable(getResources().getDrawable(objects.get(position).getResId()));
            titleTextView.setText(objects.get(position).getQuestionTitle());

            return view;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //获得数据库对象
        harbinMetroDB = HarbinMetroDB.getInstance(this);

        contact_activity_toolbar = findViewById(R.id.contact_activity_toolbar);
        contact_activity_toolbar.setTitle("热心帮助");
        contact_activity_toolbar.setLogo(R.drawable.ic_action_contact_assistant);
        setSupportActionBar(contact_activity_toolbar);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);//通过该方法设置一个导航按钮图标
        }

        objects = new ArrayList<QuestionInfo>();

        contact_activity_button = findViewById(R.id.contact_activity_contact_button);
        contact_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//拨打电话（跳转到拨号界面，用户手动点击拨打）
                Toast.makeText(ContactActivity.this,"你点击了联系客服",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "18846074770");
                intent.setData(data);
                startActivity(intent);
            }
        });

        //objects = (ArrayList<QuestionInfo>) harbinMetroDB.loadQuestionInfo();

        //if(objects == null) {//如果从SQLite获得的数据为空，向服务器发送请求
            //向服务器发送获取热心帮助数据请求
            getDataFromServer();
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
     * 对返回的接送数据进行解析  并且返回一个集合，集合的内容是一个热心帮助的详情信息
     *
     * @param result
     * @return 复习一下最原始的JSONObject解析方式
     * 使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
     */
    private ArrayList<QuestionInfo> parseData(String result) {
        try {
            //JSONObject jo1 = new JSONObject(result);
            //String joError = jo1.getString("reason");
            //if (joError.equals(RESULT_SUCCESS)) {
            // 接口成功返回数据
            //JSONObject jo2 = jo1.getJSONObject("result");
            //JSONArray ja = jo2.getJSONArray("data");
            JSONObject jo1 = new JSONObject(result);
            JSONArray ja = jo1.getJSONArray("params");
            ArrayList<QuestionInfo> InfoList = new ArrayList<QuestionInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                QuestionInfo info = new QuestionInfo();
                info.setId(jo.getString("id"));
                info.setQuestionTitle(jo.getString("questionTitle"));
                info.setAnswer(jo.getString("answer"));
                info.setResId(jo.getInt("resId"));
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
     * 发送http请求获取热心帮助数据
     *
     * @param
     */
    public void getDataFromServer() {
        // 一个失物招领类型的访问请求。
        String newsUrl = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/GetQuestionServlet";
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
                Toast.makeText(ContactActivity.this, "连接请求失败！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ContactActivity.this, "json解析错误！", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    //将从服务器获得的数据存储到本地SQLite的lostfindtable
                    DBUtil.handleQuestionResponse(harbinMetroDB, objects);
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
                            //设置图标
                            objects.get(0).setResId(R.drawable.ic_number_1);
                            objects.get(1).setResId(R.drawable.ic_number_2);
                            objects.get(2).setResId(R.drawable.ic_number_3);
                            objects.get(3).setResId(R.drawable.ic_number_4);
                            objects.get(4).setResId(R.drawable.ic_number_5);
                            objects.get(5).setResId(R.drawable.ic_number_6);
                            objects.get(6).setResId(R.drawable.ic_number_7);
                            objects.get(7).setResId(R.drawable.ic_number_8);
                            objects.get(8).setResId(R.drawable.ic_number_9);
                            objects.get(9).setResId(R.drawable.ic_number_9p);

                            contact_activity_listview = findViewById(R.id.contact_activity_listview);
                            contact_activity_listview.setAdapter(questionAdapter);
                            contact_activity_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(ContactActivity.this,"你点击了"+(position+1)+"号问题",Toast.LENGTH_SHORT).show();
                                    Intent intent =  new Intent(ContactActivity.this,QuestionInfoActivity.class);
                                    intent.putExtra("question",objects.get(position));
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
