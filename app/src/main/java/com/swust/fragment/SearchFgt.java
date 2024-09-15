package com.swust.fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.swust.R;
import com.swust.adapter.CommonAdapter;
import com.swust.pojo.Word;
import com.swust.utils.Const;


import java.util.List;

public class SearchFgt extends ListFragment {

    private CommonAdapter mCommonAdapter;
    private onSearchClickListener mOnSearchClickListener;

    public static SearchFgt newInstance(String metaKey) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.META_KEY, metaKey);
        SearchFgt searchFgt = new SearchFgt();
        searchFgt.setArguments(bundle);
        return searchFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSearchClickListener) {
            mOnSearchClickListener = (onSearchClickListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommonAdapter = new CommonAdapter<Word>(null, R.layout.item_word) {
            @Override
            public void bindView(ViewHolder holder, Word obj) {
                holder.setText(R.id.tv_item_word, obj.getKey());
            }
        };
        setListAdapter(mCommonAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnSearchClickListener = null;
    }

    public void refresh(List<Word> wordList) {
        mCommonAdapter.setData(wordList);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.getWord((Word) l.getItemAtPosition(position));
        }
    }

    public interface onSearchClickListener {
        void getWord(Word word);
    }
}
