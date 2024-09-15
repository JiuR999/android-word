package com.swust.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.swust.R;
import com.swust.db.WordDBOpenHelper;
import com.swust.fragment.FunctionFgt;
import com.swust.pojo.User;
import com.swust.utils.Const;

public class MainActivity extends AppCompatActivity {
    private FunctionFgt functionFgt;
    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private long exitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            String name = getIntent().getStringExtra(Const.NAME);
            String account = getIntent().getStringExtra(Const.ACCOUNT);
            String pwd = getIntent().getStringExtra(Const.PASSWORD);
            User user = new User(account,pwd,name);
            String tableName = Const.WORDS_USER + account;
            new Thread(()->{
                WordDBOpenHelper instance = WordDBOpenHelper.getInstance(this);
                SQLiteDatabase database = instance.getDatabase();
                String sqlCreateTable = "create table if not exists " + tableName + " ("
                        + "Word_Id integer PRIMARY KEY AUTOINCREMENT, "
                        + "Word_Key text,"
                        + "Word_Phono text,"
                        + "Word_Trans text,"
                        + "Word_Example text"
                        + ");";
                database.execSQL(sqlCreateTable);
            }).start();
            functionFgt = FunctionFgt.newInstance(user);
            mFragmentManager.beginTransaction().add(R.id.unit_content, functionFgt).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, R.string.exit_hint, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}