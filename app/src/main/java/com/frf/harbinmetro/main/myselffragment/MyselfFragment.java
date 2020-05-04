package com.frf.harbinmetro.main.myselffragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.aboutus.AboutUsActivity;
import com.frf.harbinmetro.contact.ContactActivity;
import com.frf.harbinmetro.explain.ExplainActivity;
import com.frf.harbinmetro.goods.GoodsActivity;
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.lostfind.LostFindActivity;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.messageboard.MessageBoardActivity;
import com.frf.harbinmetro.metromap.MetroMapActivity;
import com.frf.harbinmetro.myinfo.MyInfoActivity;
import com.frf.harbinmetro.myinfo.model.MyInfoSelect;
import com.frf.harbinmetro.notification.NotificationActivity;
import com.frf.harbinmetro.outgoing.OutgoingActivity;
import com.frf.harbinmetro.searchline.SearchLineActivity;
import com.frf.harbinmetro.searchstation.SearchStationActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MyselfFragment extends Fragment {

    private CircleImageView myself_fragment_head_imageview;//头像

    private CardView cardView_myinfo;//个人信息
    private CardView cardView_goods;//我的订单
    private CardView cardView_outgoing;//出行记录
    private CardView cardView_about_us;//关于我们
    private CardView cardView_explain;//相关说明
    private CardView cardView_message_board;//乘客留言

    private Button myself_fragment_quit_button;//退出账号按钮
    private TextView myself_fragment_header_username;//头像下面的用户名

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myself_fragment_head_imageview = getActivity().findViewById(R.id.myself_fragment_head_imageview);//头像
        myself_fragment_header_username = getActivity().findViewById(R.id.myself_fragment_head_textview);//头像下面的用户名
        cardView_myinfo = getActivity().findViewById(R.id.myself_fragment_cardview1);//个人信息
        cardView_goods = getActivity().findViewById(R.id.myself_fragment_cardview2);//我的订单
        cardView_outgoing = getActivity().findViewById(R.id.myself_fragment_cardview3);//出行记录
        cardView_about_us = getActivity().findViewById(R.id.myself_fragment_cardview4);//关于我们
        cardView_explain = getActivity().findViewById(R.id.myself_fragment_cardview5);//相关说明
        cardView_message_board = getActivity().findViewById(R.id.myself_fragment_cardview6);//乘客留言
        myself_fragment_quit_button = getActivity().findViewById(R.id.myself_fragment_quit_button);//退出账号按钮

        myself_fragment_head_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState() == false){//未登陆状态
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{//登陆状态
                    Toast.makeText(getActivity(),"你已经登陆！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardView_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState()==true){//登陆
                    Toast.makeText(getActivity(), "你点击了个人信息", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getActivity(), MyInfoActivity.class);
                    startActivity(intent1);
                }else{//未登录
                    Toast.makeText(getActivity(), "您还未登陆，请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cardView_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState()==true){//登陆
                    Toast.makeText(getActivity(), "你点击了我的订单", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getActivity(), GoodsActivity.class);
                    startActivity(intent2);
                }else{//未登录
                    Toast.makeText(getActivity(), "您还未登陆，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardView_outgoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState()==true){//登陆
                    Toast.makeText(getActivity(), "你点击了出行记录", Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(getActivity(), OutgoingActivity.class);
                    startActivity(intent3);
                }else{//未登录
                    Toast.makeText(getActivity(), "您还未登陆，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardView_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了关于我们", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent4);
            }
        });
        cardView_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "你点击了相关说明", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(getActivity(), ExplainActivity.class);
                startActivity(intent5);
            }
        });
        cardView_message_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState()==true){//登陆
                    Toast.makeText(getActivity(), "你点击了乘客留言", Toast.LENGTH_SHORT).show();
                    Intent intent6 = new Intent(getActivity(), MessageBoardActivity.class);
                    startActivity(intent6);
                }else{//未登录
                    Toast.makeText(getActivity(), "您还未登陆，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myself_fragment_quit_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(MyApplication.isLoginState()){//已登陆
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("退出账号")
                            .setMessage("是否退出账号")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) {
                                    //finish();
                                    MyApplication.setLoginState(false);
                                    //获取MainActivity的UI组件，更新MainActivity的UI
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.onResume();
                                    //更新Fragment的UI
                                    onResume();
                                    Toast.makeText(getActivity(),"账号退出成功",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            })
                            .create();
                    alertDialog.show();
                }else{//未登陆
                    Toast.makeText(getActivity(),"请先登陆",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {//要善于利用Activity和Fragment的生命周期实现UI的变化，利用Android系统提供的Application类实现共享数据，活灵活用
        super.onResume();
        //获取MainActivity的UI组件
//        MainActivity mainActivity = (MainActivity) getActivity();
//        NavigationView navView = mainActivity.navView;
//        View headerLayout = mainActivity.headerLayout;
//        TextView username = mainActivity.username;
//        TextView email = mainActivity.email;
//        CircleImageView headImageView = mainActivity.headImageView;
//        mainActivity.onResume();
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        if(myApplication.isLoginState()){//登陆状态
            SharedPreferences preferences = getActivity().getSharedPreferences("passenger", MODE_PRIVATE);
            myself_fragment_header_username.setText(preferences.getString("username",""));
            Glide.with(Utils.getContext()).load(preferences.getString("imageurl","")).into(myself_fragment_head_imageview);
            //myself_fragment_header_username.setText(MyApplication.getPassenger().getUsername());
            //Glide.with(Utils.getContext()).load(MyApplication.getPassenger().getImageurl()).into(myself_fragment_head_imageview);

//            username.setText(MyApplication.getPassenger().getUsername());
//            email.setText(MyApplication.getPassenger().getEmail());
//            Glide.with(Utils.getContext()).load(MyApplication.getPassenger().getImageurl()).into(headImageView);
        }else{//未登陆状态
            myself_fragment_header_username.setText("请登陆");
            myself_fragment_head_imageview.setImageDrawable(myApplication.getDrawable(R.mipmap.harbin_metro_logo_round));

//            username.setText("用户名");
//            email.setText("邮箱");
//            headImageView.setImageDrawable(myApplication.getDrawable(R.mipmap.harbin_metro_logo_round));
        }
    }


}
