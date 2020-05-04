package com.frf.harbinmetro.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 工具类,
 * 网络很多的，可以不用理解只要知道有这个东西就好了
 *
 * @author 方荣福
 */
public class Utils {

    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 根据id获取字符串
     */
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 根据id获取字符串数组
     */
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * 加载布局文件
     */
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 判断当前是否运行在主线程
     *
     * @return
     */
    public static boolean isRunOnUiThread() {
        return getMainThreadId() == android.os.Process.myTid();
    }

    /**
     * 网络请求工具
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 通过Post进行传参，发送请求
     * @param address
     * @param callback
     * @param userid
     */
    public static void sendOkHttpRequestPost(String address, okhttp3.Callback callback,String userid){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid",userid);//传递键值对参数
        Request request =  new Request.Builder()//创建request对象
                .url(address)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 保证当前的操作运行在UI主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isRunOnUiThread()) {
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }
}