package com.swust.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.swust.R;
import com.swust.fragment.WordListFgt;
import com.swust.pojo.Unit;
import com.swust.utils.Const;


public class WordListActivity extends AppCompatActivity implements WordListFgt.onWordClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Unit unit = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            unit = getIntent().getParcelableExtra(Const.UNIT_KEY, Unit.class);
        }else {
            unit = getIntent().getParcelableExtra(Const.UNIT_KEY);
        }
        if (unit != null) {
            String cat = unit.getMetaKey();
            Log.d("WordList",cat);
            setTitle(cat.substring(cat.indexOf("_") + 1, cat.length()) + " - Unit - " + unit.getKey());
            if (savedInstanceState == null) {
                WordListFgt wordListFgt = WordListFgt.newInstance(unit.getMetaKey(), unit.getKey());
                getSupportFragmentManager().beginTransaction().add(R.id.unit_content, wordListFgt).commit();
            }
        } else {
            String account = getIntent().getStringExtra(Const.ACCOUNT);
            if (savedInstanceState == null) {
                WordListFgt wordListFgt = WordListFgt.newInstance(Const.WORDS_USER + account,null);
                getSupportFragmentManager().beginTransaction().add(R.id.unit_content, wordListFgt).commit();
            }
            String title = getIntent().getStringExtra(Const.META_KEY);
            setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_item_search);
        item.setVisible(false);
        /*mSearchView = (SearchView) item.getActionView();
        if (mSearchView != null) {
            mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
            mSearchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                    mWordDao = new WordDao(UnitListActivity.this);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.hide(mUnitListFgt);
                    if (mSearchFgt == null) {
                        mSearchFgt = SearchFgt.newInstance(mMetaKey);
                        transaction.add(R.id.unit_content, mSearchFgt);
                    } else {
                        transaction.show(mSearchFgt);
                    }
                    transaction.commit();
                }
            });
            mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.hide(mSearchFgt);
                    transaction.show(mUnitListFgt);
                    transaction.commit();
                    return false;
                }
            });
            mSearchView.setQueryHint(getString(R.string.search_hint));
            mSearchView.setOnQueryTextListener(this);
        }*/
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getWordList(String metaKey, int unitKey, int wordKey) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Const.META_KEY, metaKey);
        //单元
        intent.putExtra(Const.UNIT_KEY, unitKey);
        //词汇
        intent.putExtra(Const.WORD_KEY, wordKey);
        startActivity(intent);
    }
}
