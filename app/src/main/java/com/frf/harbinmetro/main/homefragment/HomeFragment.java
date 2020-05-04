package com.frf.harbinmetro.main.homefragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.frf.harbinmetro.contact.ContactActivity;
import com.frf.harbinmetro.lostfind.LostFindActivity;
import com.frf.harbinmetro.main.model.NewsInfo;
import com.frf.harbinmetro.notification.NotificationActivity;
import com.frf.harbinmetro.saleticket.SaleTicketActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;
import com.frf.harbinmetro.searchstation.SearchStationActivity;
import com.frf.harbinmetro.util.Utils;
import com.frf.harbinmetro.web.WebActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

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
    private Banner banner;//轮询广告牌
    private ArrayList<String> list_path;//用来存储广告图片路径
    private ArrayList<String> list_title;//用来存储广告图标题目

    private CardView cardView_sale_ticket;//地铁购票卡片
    private CardView cardView_search_line;//线路查询卡片
    private CardView cardView_search_station;//站点查询卡片
    private CardView cardView_news_notification;//地铁公告卡片
    private CardView cardView_lost_find;//失物招领卡片
    private CardView cardView_contact_assistant;//热心帮助卡片

    private ArrayList<NewsInfo> objects;///存储广告
    private ListView newsListview;//新闻列表
    private BaseAdapter newsAdapter = new BaseAdapter() {//新闻列表适配器

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
                view = View.inflate(getActivity(), R.layout.main_activity_news_items, null);
            } else {
                view = convertView;
            }

            AppCompatImageView newsImageView = view.findViewById(R.id.main_activity_news_item_imageview);
            TextView newsTextView = view.findViewById(R.id.main_activity_news_item_textview);

            Glide.with(Utils.getContext()).load(objects.get(position).getPic()).into(newsImageView);
            newsTextView.setText(objects.get(position).getTitle());

            return view;
        }
    };//新闻列表的适配器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBanner();//初始化轮询广告牌

        //新闻列表适配
        objects = new ArrayList<NewsInfo>();
//        for(int i=1;i<=10;i++){
//            NewsInfo newsInfo = new NewsInfo();
//            newsInfo.setResId(R.drawable.ic_action_news);
//            newsInfo.setTitle("哈尔滨地铁新闻"+i);
//            objects.add(newsInfo);
//        }
        //通过okhttp请求数据
        getDataFromServer("top");//头条新闻


//        newsListview=getActivity().findViewById(R.id.main_activity_listview_news);
//        newsListview.setAdapter(newsAdapter);
//        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(getActivity(), "你点击了"+arg2+"新闻", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        cardView_sale_ticket = getActivity().findViewById(R.id.main_activity_card_view_sale_ticket);//地铁购票卡片
        cardView_search_line = getActivity().findViewById(R.id.main_activity_card_view_search_line);//线路查询卡片
        cardView_search_station = getActivity().findViewById(R.id.main_activity_card_view_search_station);//站点查询卡片
        cardView_news_notification = getActivity().findViewById(R.id.main_activity_card_view_notification);//地铁公告卡片
        cardView_lost_find = getActivity().findViewById(R.id.main_activity_card_view_lost_find);//失物招领卡片
        cardView_contact_assistant = getActivity().findViewById(R.id.main_activity_card_view_contact_assistant);//热心帮助卡片
        cardView_sale_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了地铁购票功能", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), SaleTicketActivity.class);
                startActivity(intent1);
            }
        });
        cardView_search_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了线路查询功能", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(), SearchLineActivity.class);
                startActivity(intent2);
            }
        });
        cardView_search_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了站点查询功能", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(getActivity(), SearchStationActivity.class);
                startActivity(intent3);
            }
        });
        cardView_news_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了地铁公告功能", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent4);
            }
        });
        cardView_lost_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了失物招领功能", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(getActivity(), LostFindActivity.class);
                startActivity(intent5);
            }
        });
        cardView_contact_assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了热心连线功能", Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent6);
            }
        });
    }

    private void initBanner() {
        banner = (Banner) getActivity().findViewById(R.id.main_activity_banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_path.add("http://www.cnad.com/upload3/Image/2019/0523/201905231745257987.jpg");
        list_path.add("http://www.cnad.com/upload3/Image/2019/0417/201904171923465872.jpg");
        list_path.add("http://www.cnad.com/upload3/Image/2019/0517/201905171631162351.jpg");
        list_path.add("http://www.cnad.com/upload3/Image/2019/0416/201904161748539173.jpg");
        list_title.add("长城凤凰杯");
        list_title.add("中国广告四十年名人访谈录");
        list_title.add("五芳斋最新广告涉嫌抄袭？");
        list_title.add("这些方法让内容营销效果好到飞起");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE); //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER) //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(new OnBannerListener() {//轮播图的监听方法
                    @Override
                    public void OnBannerClick(int position) {
                        //Toast.makeText(getActivity(), "你点了第" + position + "张轮播图", Toast.LENGTH_SHORT).show();
                        switch(position){
                            case 0:
                                Intent intent1 = new Intent(getActivity(), WebActivity.class);
                                intent1.putExtra("url", "http://2019fhb.cnad.cn/");
                                startActivity(intent1);
                                break;
                            case 1:
                                Intent intent2 = new Intent(getActivity(), WebActivity.class);
                                intent2.putExtra("url", "http://www.cnad.com/html/special/2/483/index.shtml");
                                startActivity(intent2);
                                break;
                            case 2:
                                Intent intent3 = new Intent(getActivity(), WebActivity.class);
                                intent3.putExtra("url", "http://www.cnad.com/show/12/299330.html");
                                startActivity(intent3);
                                break;
                            case 3:
                                Intent intent4 = new Intent(getActivity(), WebActivity.class);
                                intent4.putExtra("url", "http://www.cnad.com/show/12/298763.html");
                                startActivity(intent4);
                                break;
                                default:
                                    break;
                        }

                    }
                })
                //必须最后调用的方法，启动轮播图。
                .start();
    }

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
                            newsListview = getActivity().findViewById(R.id.main_activity_listview_news);
                            newsListview.setAdapter(newsAdapter);
                            newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    // TODO Auto-generated method stub

                                    //Toast.makeText(getActivity(), "你点击了"+arg2+"新闻", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), WebActivity.class);
                                    intent.putExtra("url", objects.get(arg2).getUrl().toString());
                                    startActivity(intent);
                                }
                            });
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

    //自定义的图片加载器
    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide是一个图片加载框架，具体我也不懂
            Glide.with(context).load((String) path).into(imageView);
        }
    }

}
