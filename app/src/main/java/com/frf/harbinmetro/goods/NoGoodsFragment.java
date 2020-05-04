package com.frf.harbinmetro.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.goods.model.Goods;
import com.frf.harbinmetro.goodsinfo.GoodsInfoActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NoGoodsFragment extends Fragment {

    private String responseText;//请求响应内容
    private ListView nogoodslist;
    private ArrayList<Goods> objects;///存储订单信息
    private TextView textView;//提示信息
    private BaseAdapter nogoodsAdapter = new BaseAdapter() {//订单列表适配器

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
                view = View.inflate(getActivity(), R.layout.yes_goods_fragment_listview_items, null);
            } else {
                view = convertView;
            }

            TextView startstation = view.findViewById(R.id.yes_goods_fragment_listview_item_startstation);
            TextView finishstation = view.findViewById(R.id.yes_goods_fragment_listview_item_finishstation);
            TextView ticketprice = view.findViewById(R.id.yes_goods_fragment_listview_item_ticketprice_value);
            TextView datetime = view.findViewById(R.id.yes_goods_fragment_listview_item_datetime_value);

            startstation.setText(objects.get(position).getStartstation());
            finishstation.setText(objects.get(position).getFinishstation());
            ticketprice.setText("¥"+objects.get(position).getTicketprice()+".00");
            datetime.setText(objects.get(position).getDatetime());

            return view;
        }
    };//订单列表的适配器

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_goods, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //订单列表适配
        objects = new ArrayList<Goods>();
        //向服务器发送获得历史订单请求
        getDataFromServer();

        textView = getActivity().findViewById(R.id.no_goods_fragment_textview);
    }

    /**
     * 对返回的接送数据进行解析  并且返回一个集合，集合的内容是一个历史订单的详情信息
     *
     * @param result
     * @return 复习一下最原始的JSONObject解析方式
     * 使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
     */
    private ArrayList<Goods> parseData(String result) {
        try {
            //JSONObject jo1 = new JSONObject(result);
            //String joError = jo1.getString("reason");
            //if (joError.equals(RESULT_SUCCESS)) {
            // 接口成功返回数据
            //JSONObject jo2 = jo1.getJSONObject("result");
            //JSONArray ja = jo2.getJSONArray("data");
            JSONObject jo1 = new JSONObject(result);
            JSONArray ja = jo1.getJSONArray("params");
            ArrayList<Goods> InfoList = new ArrayList<Goods>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Goods info = new Goods();
                info.setGoodsid(jo.getString("goodsid"));
                info.setOfuserid(jo.getString("ofuserid"));
                info.setLineid(jo.getString("lineid"));
                info.setDatetime(jo.getString("datetime"));
                info.setState(jo.getString("state"));
                info.setStartstation(jo.getString("startStation"));
                info.setFinishstation(jo.getString("endStation"));
                info.setTicketprice(jo.getString("ticketprice"));
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
     * 发送http请求获取历史订单数据
     *
     * @param
     */
    public void getDataFromServer() {
        // 一个Goods类型的访问请求。
        String newsUrl = "http://HaerbinStation.free.idcfengye.com/AppHarbinMetro/GetNoGoodsServlet";
        Log.d("jiaBing", "请求的连接为" + newsUrl);
        //带着这个请求去访问。 就和你复制一个网址用电脑访问是一样的，比如复制下面的在浏览器上访问
        // http://v.juhe.cn/toutiao/index?type=toutiao&key=3dc86b09a2ee2477a5baa80ee70fcdf5
        Utils.sendOkHttpRequestPost(newsUrl, new Callback() {
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
                            if(objects.size()==0){
                                textView.setVisibility(View.VISIBLE);
                            }else{
                                textView.setVisibility(View.GONE);
                            }
                            nogoodslist = getActivity().findViewById(R.id.no_goods_fragment_listview);
                            nogoodslist.setAdapter(nogoodsAdapter);
                            nogoodslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    // TODO Auto-generated method stub

                                    Toast.makeText(getActivity(),"你点击了第"+(arg2+1)+"条历史订单",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                                    //Bundle bundle = new Bundle();
                                    //bundle.putParcelable("onenotification", objects.get(arg2));
                                    //intent.putExtra("bundle",bundle);
                                    intent.putExtra("goodsinfo",objects.get(arg2));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        }, MyApplication.getPassenger().getId());//设置参数
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

}
