package com.swust.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.R;
import com.swust.adapter.WordPagerAdapter;
import com.swust.db.dao.WordDao;
import com.swust.fragment.ExamFgt;
import com.swust.fragment.WordListFgt;
import com.swust.pojo.Word;
import com.swust.utils.Const;
import com.swust.utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class ExamActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private String mMetaKey;
    private int mUnitKey;
    private List<Word> mWordList;
    private int mWordPage;
    private ViewPager mViewPager;
    private WordPagerAdapter mWordPagerAdapter;
    private Handler handler;
    private ImageView btnPre,btnNext;
    private View childAt;
    private ExamFgt examFgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        WordDao wordDao = new WordDao(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        //表名
        mMetaKey = intent.getStringExtra(Const.META_KEY);
        //随机单元
        mUnitKey = new Random().nextInt(10) + 1;
        mWordPage = 0;
        //单元词汇判断
        mWordList = wordDao.getWords(mMetaKey, mUnitKey);
        setTitle((mWordPage + 1) + "/" + mWordList.size());
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mWordPagerAdapter = new WordPagerAdapter(getSupportFragmentManager(), mWordList);
        mWordPagerAdapter.setNewFragmentStratery(word-> ExamFgt.newInstance(word));
        mViewPager.setAdapter(mWordPagerAdapter);
        mViewPager.setCurrentItem(mWordPage);
        mViewPager.addOnPageChangeListener(this);
        btnPre = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnPre.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mWordPage = position;
        setTitle((mWordPage + 1) + "/" + mWordList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_prev){
            if (mWordPage - 1 < 0) {
                Toast.makeText(this, R.string.first_page, Toast.LENGTH_SHORT).show();
            } else {
                mWordPage--;
                mViewPager.setCurrentItem(mWordPage);
            }
        } else if (id == R.id.btn_next) {
                if (mWordPage + 1 >= mWordList.size()) {
                    Toast.makeText(this, R.string.last_page, Toast.LENGTH_SHORT).show();
                }else {
                    mWordPage++;
                    mViewPager.setCurrentItem(mWordPage);
                }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}