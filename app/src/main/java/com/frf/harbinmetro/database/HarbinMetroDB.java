package com.frf.harbinmetro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.frf.harbinmetro.contact.model.QuestionInfo;
import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.notification.model.NotificationInfo;

import java.util.ArrayList;
import java.util.List;

public class HarbinMetroDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME="harbinmetro_db";

    /**
     * 数据库版本
     */
    public static final int VERSION=1;

    private static HarbinMetroDB harbinMetroDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     * HarbinMetroDB是一个单例类，将其构造函数私有化，
     */
    private HarbinMetroDB(Context context) {
        HarbinMetroOpenHelper dbHelper=new HarbinMetroOpenHelper(context, DB_NAME, null, VERSION);
        db=dbHelper.getWritableDatabase();//根据dbHelper.getWritableDatabase()获取数据库实例对象
    }

    /**
     * 获取HarbinMetroDB的实例
     * 保证程序始终只调用这一个数据库，不会另外再创建，浪费内存空间
     * 并通过getInstance()方法获取HarbinMetroDB实例，可以保证全局范围内只会有一个AfWeather实例
     */
    public synchronized static HarbinMetroDB getInstance(Context context) {
        if(harbinMetroDB == null) {
            harbinMetroDB=new HarbinMetroDB(context);
        }
        return harbinMetroDB;

    }

    /**
     * 将LostFindInfo实例存储到数据库
     */
    public void saveLostFindInfo(LostFindInfo lostFindInfo) {
        if(lostFindInfo!=null) {
            ContentValues values=new ContentValues();
            values.put("id", lostFindInfo.getId());
            values.put("title", lostFindInfo.getTitle());
            values.put("feature", lostFindInfo.getFeature());
            values.put("address", lostFindInfo.getAddress());
            values.put("date", lostFindInfo.getDateTime());
            values.put("state", lostFindInfo.getState());
            values.put("publisher", lostFindInfo.getPublisher());
            values.put("imageurl", lostFindInfo.getImageurl());
            db.insert("lostfindtable", null, values);
        }
    }

    /**
     * 从数据库读取所有失物招领的信息
     */
    public List<LostFindInfo> loadLostFindInfo(){
        List<LostFindInfo> list=new ArrayList<LostFindInfo>();
        Cursor cursor=db.query("lostfindtable", null, null, null, null, null, null);//数据集
        if(cursor.moveToFirst()) {
            do {
                LostFindInfo lostFindInfo=new LostFindInfo();
                lostFindInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
                lostFindInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                lostFindInfo.setFeature(cursor.getString(cursor.getColumnIndex("feature")));
                lostFindInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                lostFindInfo.setDateTime(cursor.getString(cursor.getColumnIndex("date")));
                lostFindInfo.setState(cursor.getString(cursor.getColumnIndex("state")));
                lostFindInfo.setPublisher(cursor.getString(cursor.getColumnIndex("publisher")));
                lostFindInfo.setImageurl(cursor.getString(cursor.getColumnIndex("imageurl")));
                list.add(lostFindInfo);
            }while(cursor.moveToNext()) ;
        }
        if(cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将NotificationInfo实例存储到数据库
     */
    public void saveNotificationInfo(NotificationInfo notificationInfo) {
        if(notificationInfo!=null) {
            ContentValues values=new ContentValues();
            values.put("id", notificationInfo.getId());
            values.put("title", notificationInfo.getTitle());
            values.put("content", notificationInfo.getContent());
            values.put("date", notificationInfo.getDateTime());
            values.put("publisher", notificationInfo.getPublisher());
            db.insert("notificationtable", null, values);
        }
    }

    /**
     * 从数据库读取所有通告通知的信息
     */
    public List<NotificationInfo> loadNotificationInfo(){
        List<NotificationInfo> list=new ArrayList<NotificationInfo>();
        Cursor cursor=db.query("notificationtable", null, null, null, null, null, null);//数据集
        if(cursor.moveToFirst()) {
            do {
                NotificationInfo notificationInfo=new NotificationInfo();
                notificationInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
                notificationInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                notificationInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
                notificationInfo.setDateTime(cursor.getString(cursor.getColumnIndex("date")));
                notificationInfo.setPublisher(cursor.getString(cursor.getColumnIndex("publisher")));
                list.add(notificationInfo);
            }while(cursor.moveToNext()) ;
        }
        if(cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将QuestionInfo实例存储到数据库
     */
    public void saveQuestionInfo(QuestionInfo questionInfo) {
        if(questionInfo!=null) {
            ContentValues values=new ContentValues();
            values.put("id", questionInfo.getId());
            values.put("question", questionInfo.getQuestionTitle());
            values.put("answer", questionInfo.getAnswer());
            db.insert("questiontable", null, values);
        }
    }

    /**
     * 从数据库读取所有问题的信息
     */
    public List<QuestionInfo> loadQuestionInfo(){
        List<QuestionInfo> list=new ArrayList<QuestionInfo>();
        Cursor cursor=db.query("questiontable", null, null, null, null, null, null);//数据集
        if(cursor.moveToFirst()) {
            do {
                QuestionInfo questionInfo=new QuestionInfo();
                questionInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
                questionInfo.setQuestionTitle(cursor.getString(cursor.getColumnIndex("question")));
                questionInfo.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                list.add(questionInfo);
            }while(cursor.moveToNext()) ;
        }
        if(cursor != null) {
            cursor.close();
        }
        return list;
    }

}
