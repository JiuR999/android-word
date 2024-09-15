package com.swust.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swust.utils.Const;

import java.io.File;

public class WordDBOpenHelper {
    private static WordDBOpenHelper sWordDBOpenHelper;
    private Context mContext;

    public static synchronized WordDBOpenHelper getInstance(Context context) {
        if (sWordDBOpenHelper == null) {
            sWordDBOpenHelper = new WordDBOpenHelper(context);
        }
        return sWordDBOpenHelper;
    }

    public WordDBOpenHelper(Context mContext) {
        this.mContext = mContext;
    }

    public SQLiteDatabase getDatabase() {
        String path = mContext.getDir(Const.DB_DIR, Context.MODE_PRIVATE) + File.separator + Const.DB_NAME;
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        if (db.isOpen()) {
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
                if (cursor != null && cursor.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return false;
    }

}