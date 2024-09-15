package com.swust.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.swust.R;
import com.swust.adapter.WordAdapter;
import com.swust.db.dao.WordDao;
import com.swust.pojo.Word;
import com.swust.utils.Const;
import java.util.List;

public class WordListFgt extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private onWordClickListener mOnWordClickListener;
    private  List<Word> wordList;
    private String tableName;
    private WordDao wordDao;
    private WordAdapter wordAdapter;
    public static WordListFgt newInstance(String tableName, Integer unitKey) {
        Bundle bundle = new Bundle();
        WordListFgt wordListFgt = new WordListFgt();
        //表名
        bundle.putString(Const.META_KEY, tableName);
        if(unitKey != null){
            bundle.putInt(Const.UNIT_KEY, unitKey);
        }
        wordListFgt.setArguments(bundle);
        return wordListFgt;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onWordClickListener) {
            mOnWordClickListener = (onWordClickListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = view.findViewById(android.R.id.list);
        //QuickScroll quickScroll = (QuickScroll) view.findViewById(R.id.quickscroll);
        wordDao = new WordDao(getActivity());
        Bundle bundle = getArguments();
        assert bundle != null;
        int unit = bundle.getInt(Const.UNIT_KEY);
        tableName = bundle.getString(Const.META_KEY);
        if(unit != 0){
            wordList = wordDao.getWords(tableName, unit);
        }else {
            wordList = wordDao.getWords(tableName,null);
        }
        wordAdapter = new WordAdapter(getActivity(), wordList);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        /*quickScroll.init(QuickScroll.TYPE_POPUP_WITH_HANDLE, listView, wordAdapter, QuickScroll.STYLE_HOLO);
        quickScroll.setFixedSize(1);

        quickScroll.setPopupColor(QuickScroll.BLUE_LIGHT, QuickScroll.BLUE_LIGHT_SEMITRANSPARENT, 1, Color.WHITE, 1);
        quickScroll.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 42);*/
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnWordClickListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnWordClickListener != null) {
            assert getArguments() != null;
            String metaKey = getArguments().getString(Const.META_KEY);
            int unitKey = getArguments().getInt(Const.UNIT_KEY);
            mOnWordClickListener.getWordList(metaKey,
                    unitKey, position);
            Log.d("WordListFgt",metaKey + "---" + unitKey);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("确定要删除此条单词记录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Word item = wordList.get(position);
                        //Toast.makeText(getActivity(), id1, Toast.LENGTH_SHORT).show();
                        wordDao.deleteWord(tableName,item);

                        wordList = wordDao.getWords(tableName,null);
                        wordAdapter.setmWordList(wordList);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return true;
    }

    public interface onWordClickListener {
        void getWordList(String metaKey, int unitKey, int wordKey);
    }
}
