package com.swust.adapter;


import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swust.fragment.DetailFgt;
import com.swust.pojo.Word;

import java.util.List;

public class WordPagerAdapter extends FragmentStatePagerAdapter {

    private List<Word> mWordList;
    private SparseArray<Fragment> mFragments;
    private newFragmentStratery newFragmentStratery;
    public WordPagerAdapter(FragmentManager fm, List<Word> wordList) {
        super(fm);
        mWordList = wordList;
        mFragments=new SparseArray<>(mWordList.size());
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frament = newFragmentStratery.getFrament(mWordList.get(position));
        mFragments.put(position, frament);
        return frament;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragments.remove(position);
    }

    @Override
    public int getCount() {
        return mWordList == null ?0:mWordList.size();
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }

    public void setNewFragmentStratery(WordPagerAdapter.newFragmentStratery newFragmentStratery) {
        this.newFragmentStratery = newFragmentStratery;
    }

    public interface newFragmentStratery {
        Fragment getFrament(Word word);
    }
}
