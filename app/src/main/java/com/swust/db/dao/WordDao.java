package com.swust.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swust.pojo.Word;
import com.swust.db.WordDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WordDao {
    private WordDBOpenHelper mWordDBOpenHelper;

    //Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example, Word_Unit;
    public WordDao(Context context) {
        mWordDBOpenHelper = WordDBOpenHelper.getInstance(context);
    }

    public List<Word> getWords(String tableName, Integer unitKey) {
        List<Word> words = null;
        String sql;
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        Cursor cursor = null;
        if(unitKey != null && unitKey != 0){
            sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example from " + tableName + " where Word_Unit=?";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(unitKey)});
        }else {
            sql = "select Word_Key, Word_Phono, Word_Trans, Word_Example from " + tableName ;
            cursor = db.rawQuery(sql,new String[]{});
        }
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int wordId = cursor.getColumnIndex("Word_Id");
                int id = wordId == -1 ? 0 : cursor.getInt(wordId);
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                word = new Word(id, key, phono, trans, exam, unitKey);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public ArrayList<Word> queryWords(String metaKey, String wordKey) {
        ArrayList<Word> words = null;
        String sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example, Word_Unit from "
                + metaKey + " where Word_Key like ?;";
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{wordKey + "%"});
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                int unitKey = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unitKey);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public void insertWord(String tableName,Word word){
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        String sqlInsert = "insert into " + tableName + "("
                + "Word_Key,"
                + "Word_Phono,"
                + "Word_Trans,"
                + "Word_Example"
                + ") values(?,?,?,?)";
        db.execSQL(sqlInsert,new Object[]{word.getKey(),word.getPhono(),
                word.getTrans(),word.getExample()});
    }

    public void deleteWord(String tableName,Word word){
        SQLiteDatabase db = mWordDBOpenHelper.getDatabase();
        String sqlDelete = "delete from " + tableName
                + " where Word_Key = ?";
        db.execSQL(sqlDelete,new Object[]{word.getKey()});
    }
}
