package com.swust.fragment;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.swust.R;
import com.swust.pojo.Word;
import com.swust.utils.Const;

public class DetailFgt extends Fragment {
    private onSpeechListener mOnSpeechListener;
    private ImageView mImageView;

    public static DetailFgt newInstance(Word word) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.WORD_KEY, word);
        DetailFgt detailFgt = new DetailFgt();
        detailFgt.setArguments(bundle);
        return detailFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSpeechListener) {
            mOnSpeechListener = (onSpeechListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tvExample = view.findViewById(R.id.tv_exam);
        TextView tvKey = view.findViewById(R.id.tv_key);
        TextView tvPhono = view.findViewById(R.id.tv_phono);
        TextView tvTrans = view.findViewById(R.id.tv_trans);
        final Word word = getArguments().getParcelable(Const.WORD_KEY);
        mImageView = view.findViewById(R.id.icon_speech);
        mImageView.setOnClickListener(v -> {
            if (mOnSpeechListener != null) {
                mOnSpeechListener.speech(word);
            }
        });
        if (word != null) {
            tvExample.setText(word.getExample());
            tvKey.setText(word.getKey());
            tvPhono.setText("[" + word.getPhono() + "]");
            tvTrans.setText(word.getTrans());
        }
        return view;
    }

    public void setSpeakImg(int resId) {
        mImageView.setImageResource(resId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnSpeechListener = null;
    }

    public interface onSpeechListener {
        void speech(Word word);
    }
}
