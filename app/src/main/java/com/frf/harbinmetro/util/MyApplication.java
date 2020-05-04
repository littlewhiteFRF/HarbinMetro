package com.frf.harbinmetro.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;

import com.frf.harbinmetro.R;
import com.frf.harbinmetro.login.model.Passenger;

/**
 * 项目的Application
 * 这个东西是提供全局上下文的。 可以不用很理解
 *
 * @author 方荣福
 * @date 2019/6/3
 */
public class MyApplication extends Application {
    private static Context context;
    private static int mainThreadId;
    private static Handler handler;

    private static boolean loginState;//设置一个静态变量，登陆状态，作为全局变量
    private static Passenger passenger;//设置一个静态变量，当前乘客的个人信息，作为全局变量

    public static void setPassenger(Passenger passenger) {
        MyApplication.passenger = passenger;
    }

    public static Passenger getPassenger() {
        return passenger;
    }

    public static boolean isLoginState() {
        return loginState;
    }

    public static void setLoginState(boolean loginState) {
        MyApplication.loginState = loginState;
    }

    public static Context getContext() {
        return context;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // 获取当前主线程id
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();

        //初始登陆状态为false，登陆后才为true
        loginState = false;
    }
}
