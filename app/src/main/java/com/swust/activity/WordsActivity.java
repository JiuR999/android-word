package com.swust.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.swust.R;
import com.swust.utils.Const;
import com.swust.utils.PlaySoundUtils;


public class WordsActivity extends AppCompatActivity implements View.OnClickListener {

    private long exitTime = 0;
    private ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        String title = getIntent().getStringExtra(Const.META_KEY);
        setTitle(title);
        findViewById(R.id.iv_cet4).setOnClickListener(this);
        findViewById(R.id.iv_cet6).setOnClickListener(this);
        findViewById(R.id.iv_gre).setOnClickListener(this);
        findViewById(R.id.iv_ietsl).setOnClickListener(this);
        findViewById(R.id.iv_nmet).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        PlaySoundUtils.playSound(this);
        Intent intent = new Intent();
        int id = v.getId();
        if(id == R.id.iv_nmet){
            intent.setClass(this, UnitListActivity.class);
            intent.putExtra(Const.META_KEY, Const.WORDS_NMET);
        } else if(id == R.id.iv_cet4){
            intent.setClass(this, UnitListActivity.class);
            intent.putExtra(Const.META_KEY, Const.WORDS_CET4);
        } else if (id == R.id.iv_cet6) {
            intent.setClass(this, UnitListActivity.class);
            intent.putExtra(Const.META_KEY, Const.WORDS_CET6);
        } else if (id == R.id.iv_ietsl) {
            intent.setClass(this, UnitListActivity.class);
            intent.putExtra(Const.META_KEY, Const.WORDS_IETSL);
        } else if (id == R.id.iv_gre) {
            intent.setClass(this, UnitListActivity.class);
            intent.putExtra(Const.META_KEY, Const.WORDS_GRE);
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }
}