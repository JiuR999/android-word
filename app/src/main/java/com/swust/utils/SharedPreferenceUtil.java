package com.swust.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;

/**
 * @author ASUS
 */
public class SharedPreferenceUtil {
    public static enum Type{INT,STRING, BOOLEAN}
    public static void save(Context context,Type type,String fileName,String key,Object obj){
        SharedPreferences spf = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        if(type == Type.INT){
            editor.putInt(key, (Integer) obj);
        } else if (type == Type.STRING) {
            editor.putString(key, (String) obj);
        } else if (type == Type.BOOLEAN) {
            editor.putBoolean(key, (Boolean) obj);
        }
        editor.apply();
    }
    public static void clearData(Context context,String fileName) {
        SharedPreferences spf = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.clear();
        editor.apply();
    }
    public static Object readData(Context context,Type type,String fileName,String key){
        SharedPreferences spf = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        if(type == Type.INT){
            return spf.getInt(key,-1);
        } else if(type == Type.STRING){
            return spf.getString(key,"");
        } else if(type == Type.BOOLEAN){
            return spf.getBoolean(key,false);
        }
        return null;
    }
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(System.currentTimeMillis());
    }
}
