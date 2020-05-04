package com.frf.harbinmetro.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.addmoney.AddMoneyActivity;
import com.frf.harbinmetro.bindaccount.BindAccountActivity;
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.main.homefragment.HomeFragment;
import com.frf.harbinmetro.main.myselffragment.MyselfFragment;
import com.frf.harbinmetro.main.newsfragment.NewsFragment;
import com.frf.harbinmetro.main.searchfragment.SearchFragment;
import com.frf.harbinmetro.searchmoney.SearchMoneyActivity;
import com.frf.harbinmetro.setting.SettingActivity;
import com.frf.harbinmetro.util.MyApplication;
import com.frf.harbinmetro.util.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar main_activity_toolbar;//顶部导航栏

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NewsFragment newsFragment;
    private MyselfFragment myselfFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    public TextView username;
    public TextView email;
    public DrawerLayout mDrawerLayer;//抽屉布局
    public NavigationView navView;//View控件
    public View headerLayout;//抽屉头部布局
    public CircleImageView headImageView;//抽屉头像

    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_activity_bottom_actionbar_home:
//                    finish();
//                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent1);
                    if (lastfragment != 0) {
                        main_activity_toolbar.setTitle("主页");
                        main_activity_toolbar.setLogo(R.drawable.ic_action_home);
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    Toast.makeText(MainActivity.this, "点击主页成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_activity_bottom_actionbar_search:
//                    finish();
//                    Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent2);
                    if (lastfragment != 1) {
                        main_activity_toolbar.setTitle("查询");
                        main_activity_toolbar.setLogo(R.drawable.ic_action_search);
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    Toast.makeText(MainActivity.this, "点击查询成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_activity_bottom_actionbar_news:
//                    finish();
//                    Intent intent3 = new Intent(MainActivity.this, NewsActivity.class);
//                    startActivity(intent3);
                    if (lastfragment != 2) {
                        main_activity_toolbar.setTitle("资讯");
                        main_activity_toolbar.setLogo(R.drawable.ic_action_news);
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    Toast.makeText(MainActivity.this, "点击资讯成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_activity_bottom_actionbar_myself:
//                    finish();
//                    Intent intent4 = new Intent(MainActivity.this, RegisterActivity.class);
//                    startActivity(intent4);
                    if (lastfragment != 3) {
                        main_activity_toolbar.setTitle("我的");
                        main_activity_toolbar.setLogo(R.drawable.ic_action_myself);
                        switchFragment(lastfragment, 3);
                        lastfragment = 3;
                    }
                    Toast.makeText(MainActivity.this, "点击我的成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);

        //加载Fragment
        initFragment();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(changeFragment);

        main_activity_toolbar = findViewById(R.id.main_toolbar);
        main_activity_toolbar.setTitle("哈尔滨地铁主页");
        main_activity_toolbar.setLogo(R.mipmap.harbin_metro_logo_round);
        setSupportActionBar(main_activity_toolbar);

        mDrawerLayer = findViewById(R.id.main_activity_drawer_layout);
        ActionBar actionBar = getSupportActionBar();//得到ActionBar的实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//通过该方法让导航按钮显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_manager);//通过该方法设置一个导航按钮图标
        }

//        mCircleImageView=findViewById(R.id.main_activity_drawerlayout_headerlayout_icon);
//        mCircleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        //因为这个circleImageView嵌套在headerlayout里，需要先找到外层的布局的id，再从这个外层布局里找到ImageView的id
        navView = findViewById(R.id.main_activity_drawer_layout_view);

        headerLayout = navView.inflateHeaderView(R.layout.main_activity_drawerlayout_headerlayout);
        headImageView = headerLayout.findViewById(R.id.main_activity_drawerlayout_headerlayout_icon);
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.isLoginState() == false){//未登陆状态
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{//登陆状态
                    Toast.makeText(MainActivity.this,"你已经登陆！",Toast.LENGTH_SHORT).show();
                }

                //changeImage();

            }
        });

        navView.setCheckedItem(R.id.main_activity_drawer_layout_bind_pay);//设置默认选项
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.main_activity_drawer_layout_bind_pay:
                        if(MyApplication.isLoginState()==true){//已登陆

                            if(MyApplication.getPassenger().getAlipayid().equals("null")){//未绑定支付账号
                                Toast.makeText(MainActivity.this, "点击了绑定支付账号选项", Toast.LENGTH_SHORT).show();
                                mDrawerLayer.closeDrawers();//选完选项后，关闭抽屉
                                //进入绑定支付账号Activity
                                Intent intent1 = new Intent(MainActivity.this, BindAccountActivity.class);
                                startActivity(intent1);
                            }else{//已绑定支付账号
                                Toast.makeText(MainActivity.this, "您已绑定支付账号", Toast.LENGTH_SHORT).show();
                            }
                        }else{//未登录
                            Toast.makeText(MainActivity.this,"请先登陆",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.main_activity_drawer_layout_money_search:
                        if(MyApplication.isLoginState()==true){//已登陆

                            if(MyApplication.getPassenger().getAlipayid().equals("null")){//未绑定支付账号
                                Toast.makeText(MainActivity.this, "请先绑定支付账号", Toast.LENGTH_SHORT).show();
                                mDrawerLayer.closeDrawers();//选完选项后，关闭抽屉
                                //进入绑定支付账号Activity
                                Intent intent1 = new Intent(MainActivity.this, BindAccountActivity.class);
                                startActivity(intent1);
                            }else{//已绑定支付账号
                                Toast.makeText(MainActivity.this, "点击了余额查询选项", Toast.LENGTH_SHORT).show();
                                mDrawerLayer.closeDrawers();//选完选项后，关闭抽屉
                                //进入余额查询界面
                                Intent intent2 = new Intent(MainActivity.this, SearchMoneyActivity.class);
                                startActivity(intent2);
                            }
                        }else{//未登录
                            Toast.makeText(MainActivity.this,"请先登陆",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.main_activity_drawer_layout_money_add:
                        if(MyApplication.isLoginState()==true){//已登陆

                            if(MyApplication.getPassenger().getAlipayid().equals("null")) {//未绑定支付账号
                                Toast.makeText(MainActivity.this, "请先绑定支付账号", Toast.LENGTH_SHORT).show();
                                mDrawerLayer.closeDrawers();//选完选项后，关闭抽屉
                                //进入绑定支付账号Activity
                                Intent intent1 = new Intent(MainActivity.this, BindAccountActivity.class);
                                startActivity(intent1);
                            }else{//已绑定支付账号
                                Toast.makeText(MainActivity.this, "点击了账户充值选项", Toast.LENGTH_SHORT).show();
                                mDrawerLayer.closeDrawers();//选完选项后，关闭抽屉
                                //进入账户充值页面
                                Intent intent3 = new Intent(MainActivity.this, AddMoneyActivity.class);
                                startActivity(intent3);
                            }
                        }else{//未登录
                            Toast.makeText(MainActivity.this,"请先登陆",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

//        login=findViewById(R.id.btn_login);
//        register=findViewById(R.id.btn_register);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    //初始化fragment和fragment数组
    private void initFragment() {
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        newsFragment = new NewsFragment();
        myselfFragment = new MyselfFragment();
        fragments = new Fragment[]{homeFragment, searchFragment, newsFragment, myselfFragment};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment, homeFragment).show(homeFragment).commit();
    }

    //切换Fragment
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.main_activity_fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_top_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://ToolBar最左侧的按钮叫做HomeAsUp按钮，而且id永远是android.R.id.home
                mDrawerLayer.openDrawer(GravityCompat.START);//打开抽屉
                break;
            case R.id.main_activity_top_toolbar_setting:
                Toast.makeText(MainActivity.this, "你点击了设置按钮", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //调用KEYCODE_BACK方法，即按下返回按钮
        if (keyCode==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.ic_action_quit);//设置小图标
            builder.setTitle("温馨提示");//设置标题
            builder.setMessage("是否退出？");//设置内容
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            //添加监听事件
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            //设置对话框按钮
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {//要善于利用Activity和Fragment的生命周期实现UI的变化，利用Android系统提供的Application类实现共享数据，活灵活用
        super.onResume();
        MyApplication myApplication = (MyApplication) MainActivity.this.getApplication();
        username = headerLayout.findViewById(R.id.username);
        email = headerLayout.findViewById(R.id.mail);
        if(myApplication.isLoginState()==true){//登陆状态
            //username.setText(MyApplication.getPassenger().getUsername());
            //email.setText(MyApplication.getPassenger().getEmail());
            //Glide.with(Utils.getContext()).load(MyApplication.getPassenger().getImageurl()).into(headImageView);
            SharedPreferences preferences = getSharedPreferences("passenger", MODE_PRIVATE);
            username.setText(preferences.getString("username",""));
            email.setText(preferences.getString("email",""));
            Glide.with(Utils.getContext()).load(preferences.getString("imageurl","")).into(headImageView);
        }else{//未登陆状态
            username.setText("用户名");
            email.setText("邮箱");
            headImageView.setImageDrawable(myApplication.getDrawable(R.mipmap.harbin_metro_logo_round));
        }

    }

}
