package com.example.pc.testeverything.SqliteManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rachel on 2019/2/20.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public final static String CREATE_NEW = "create table news(id integer primary key autoincrement,title text,message text,publishdate integer,commentcount integer)";

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
