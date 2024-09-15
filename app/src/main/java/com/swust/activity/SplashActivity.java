package com.swust.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;

/*import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;*/
import com.swust.R;
import com.swust.activity.login.LoginActivity;
import com.swust.utils.Const;
import com.swust.db.WordDBOpenHelper;
import com.swust.utils.FileUtils;


import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //SpeechUtility.createUtility(this, SpeechConstant.APPID + "=*****");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean exist = sharedPreferences.getBoolean(Const.SP_KEY, false);
        if (!exist) {
            sharedPreferences.edit().putBoolean(Const.SP_KEY, true).apply();
            new FileTask().execute();
        } else {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    startMain();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, Const.DELAY_TIME);
        }
    }

    private void startMain() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initTable() {
        WordDBOpenHelper wordDbOpenHelper = (WordDBOpenHelper) WordDBOpenHelper.getInstance(this);
        SQLiteDatabase database = wordDbOpenHelper.getDatabase();

        database.execSQL("create table if not exists TABLE_UNIT (" +
                "Unit_Key integer not null," +
                "Unit_Time integer not null default 0," +
                "Cate_Key text references TABLE_META(Meta_Key)" +
                ");");
        for (String metaKey : Const.META_KEYS) {
            Cursor cursor = database.rawQuery("select Meta_UnitCount from TABLE_META where Meta_Key=?;"
                    , new String[]{metaKey});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("Meta_UnitCount"));
                for (int i = 1; i <= count; i++) {
                    database.execSQL("insert into TABLE_UNIT (Unit_Key,Unit_Time,Cate_Key) " +
                            "values(?,?,?);", new Object[]{i, 0, metaKey});
                }
            }
            cursor.close();
        }
    }

    private class FileTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            FileUtils.writeData(SplashActivity.this);
            initTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startMain();
        }
    }
}
