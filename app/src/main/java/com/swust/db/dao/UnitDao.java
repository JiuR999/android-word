package com.swust.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swust.pojo.Unit;
import com.swust.db.WordDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UnitDao {
    private WordDBOpenHelper mWordDBOpenHelper;

    public UnitDao(Context context) {
        mWordDBOpenHelper = (WordDBOpenHelper) WordDBOpenHelper.getInstance(context);
    }

    public List<Unit> getUnits(String metaKey) {
        List<Unit> units = null;
        String sql = "select Unit_Key, Unit_Time from TABLE_UNIT where Cate_Key = ?";
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{metaKey});
        if (cursor.moveToFirst()) {
            Unit unit;
            units = new ArrayList<>(cursor.getCount());
            do {
                int key = cursor.getInt(cursor.getColumnIndex("Unit_Key"));
                long time = cursor.getLong(cursor.getColumnIndex("Unit_Time"));
                unit = new Unit(key, time, metaKey);
                units.add(unit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return units;
    }

    public void updateTime(String metaKey, int unitKey, long time) {
        String sqlUpdate = "update TABLE_UNIT set Unit_Time=? where Unit_Key=? and Cate_Key=?;";
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        db.execSQL(sqlUpdate, new Object[]{time + getTime(metaKey, unitKey), unitKey, metaKey});
    }

    public long getTime(String metaKey, int unitKey) {
        String sql = "select Unit_Time from TABLE_UNIT where Unit_Key=? and Cate_Key=?;";
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(unitKey), metaKey});
        long existTime = 0;
        if (cursor.moveToFirst()) {
            existTime = cursor.getLong(cursor.getColumnIndex("Unit_Time"));
        }
        cursor.close();
        return existTime;
    }

}
