package com.frf.harbinmetro.splash;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.frf.harbinmetro.R;
import com.frf.harbinmetro.login.LoginActivity;
import com.frf.harbinmetro.main.MainActivity;
import com.frf.harbinmetro.util.MyApplication;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final String TAG_MESSAGE = "SplashActivity";

    private final int SPLASH_DISPLAY_LENGHT = 3000; // 3秒后进入系统

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏最上面的状态栏
        //getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_splash);

        //判断偏好存储是否为空
        SharedPreferences preferences = getSharedPreferences("passenger", MODE_PRIVATE);
        String username = preferences.getString("username","");
        String password = preferences.getString("password", "");
        if(!username.equals("")&&!password.equals("")){
            MyApplication.setLoginState(true);//设置为登陆状态
        }

        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(SPLASH_DISPLAY_LENGHT);//使程序休眠(延迟)2秒
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);//启动MainActivity
                    startActivity(intent);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }

}

