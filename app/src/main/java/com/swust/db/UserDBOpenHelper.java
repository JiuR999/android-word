package com.swust.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.swust.utils.Const;

public class UserDBOpenHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper mInstance;

    public static synchronized SQLiteOpenHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new UserDBOpenHelper(context, Const.DADABASE_USER,null,1);
        }
        return mInstance;
    }
    private UserDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表users，表中包含_account和_password两个字段，为了方便我把他们的类型全部设为text
        String sql = "create table users(" + Const.ACCOUNT + " text,"
                + Const.PASSWORD + " text,"
                + Const.NAME + " text,"
                + Const.AVATAR + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}