package com.frf.harbinmetro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HarbinMetroOpenHelper extends SQLiteOpenHelper {

    /**
     * lostfind表建表语句
     */
    public static final String CREATE_LOSTFIND="create table lostfindtable ("
            +"id integer primary key autoincrement,"
            +"title text,"
            +"feature text,"
            +"address text,"
            +"date text,"
            +"publisher text,"
            +"state text,"
            +"imageurl text)";

    /**
     * notification表建表语句
     */
    public static final String CREATE_NOTIFICATION="create table notificationtable ("
            +"id integer primary key autoincrement,"
            +"title text,"
            +"content text,"
            +"date text,"
            +"publisher text)";

    /**
     * question表建表语句
     */
    public static final String CREATE_QUESTION="create table questiontable ("
            +"id integer primary key autoincrement,"
            +"question text,"
            +"answer text)";

    /**
     * 构造函数
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public HarbinMetroOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOSTFIND);//创建lostfind表
        db.execSQL(CREATE_NOTIFICATION);//创建notification表
        db.execSQL(CREATE_QUESTION);//创建question表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
