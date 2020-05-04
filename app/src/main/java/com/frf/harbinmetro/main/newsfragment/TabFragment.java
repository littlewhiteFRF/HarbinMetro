package com.frf.harbinmetro.main.newsfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.main.model.NewsInfo;
import com.frf.harbinmetro.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author 方荣福
 * @date 2019/6/4
 * 所有新闻界面Fragment的基类。 因为只是一个recycleView显示新闻，所以不用XML，只有用代码写布局
 */

public abstract class TabFragment extends Fragment {

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
    private ArrayList<NewsInfo> objects;///存储广告

    private View view;

    private String type;//新闻类型

//    public static Fragment newInstance(String newsType) {
//        this.type=newsType;
//        TabFragment fragment = new TabFragment();
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onGetNewType();
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        return view;
    }

    /**
     * 由子类实现获取具体类型新闻的方法
     *
     * @return
     */
    public abstract void onGetNewType();

    /**
     * 核心功能
     * 发送http请求获取新闻数据
     *
     * @param type 新闻的类型
     */
    public void getDataFromServer(String type) {
        // 拼接出一个新闻类型的访问请求。
        String newsUrl = "http://v.juhe.cn/toutiao/index?type=" + type + "&key=3dc86b09a2ee2477a5baa80ee70fcdf5";
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
                Toast.makeText(getActivity(), "连接请求失败！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), "json解析错误！", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
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
                            RecyclerView recyclerView = view.findViewById(R.id.recycler);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(new RecyclerAdapter(objects));
                        }
                    });
                }
            }
        });
    }

    /**
     * 对返回的接送数据进行解析  并且返回一个集合，集合的内容是一个新闻的详情信息
     *
     * @param result
     * @return 复习一下最原始的JSONObject解析方式
     * 使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
     */
    private ArrayList<NewsInfo> parseData(String result) {
        try {
            JSONObject jo1 = new JSONObject(result);
            String joError = jo1.getString("reason");
            if (joError.equals(RESULT_SUCCESS)) {
                // 接口成功返回数据
                JSONObject jo2 = jo1.getJSONObject("result");
                JSONArray ja = jo2.getJSONArray("data");
                ArrayList<NewsInfo> InfoList = new ArrayList<NewsInfo>();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo3 = ja.getJSONObject(i);
                    NewsInfo info = new NewsInfo();
                    info.setTitle(jo3.getString("title"));
                    info.setPic(jo3.getString("thumbnail_pic_s"));
                    info.setTime(jo3.getString("date"));
                    info.setUrl(jo3.getString("url"));
                    InfoList.add(info);
                }
                return InfoList;
            } else {
                // 接口返回数据失败，可通过返回的code对照出具体原因，暂不处理
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
