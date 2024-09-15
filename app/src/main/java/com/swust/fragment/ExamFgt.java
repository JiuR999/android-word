package com.swust.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.swust.R;
import com.swust.databinding.FragmentExamBinding;
import com.swust.pojo.Word;
import com.swust.utils.Const;

import java.util.Random;

public class ExamFgt extends Fragment {
    private String mAnswer,correct;
    private int mode;
    private TextView tvQuestion;
    private EditText etAnswer;
    private TextView tvCorrectAnswer;
    private Word word;
    public static ExamFgt newInstance(Word word) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.WORD_KEY, word);
        ExamFgt examFgt = new ExamFgt();
        examFgt.setArguments(bundle);
        return examFgt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentExamBinding binding = FragmentExamBinding.inflate(inflater,container,false);
        tvQuestion = binding.tvQuestion;
        etAnswer = binding.etAnswer;
        tvCorrectAnswer = binding.tvCorrectAnswer;
        binding.btnExamSubmit.setOnClickListener(v->checkAnswer(binding.getRoot()));
        this.word = getArguments().getParcelable(Const.WORD_KEY);

        if (word != null) {
            mode = new Random().nextInt(2);
            if(mode > 0){
                binding.tvQuestion.setText(word.getKey());
            } else {
                binding.tvQuestion.setText(word.getTrans());
            }
        }
        if(mAnswer != null){
            binding.etAnswer.setText(mAnswer);
        }
        return binding.getRoot();
    }
    private void checkAnswer(View childAt) {
        String mAnswer = etAnswer.getText().toString();
        if(!mAnswer.isEmpty()){
            this.setmAnswer(mAnswer);
            String correctAnswer = null;
            if(mode > 0){
                correctAnswer = word.getTrans();
            }else {
                correctAnswer = word.getKey();
            }
            if(correctAnswer.contains(mAnswer)){
                childAt.findViewById(R.id.img_exam_correct).setVisibility(View.VISIBLE);
            }else {
                childAt.findViewById(R.id.img_exam_error).setVisibility(View.VISIBLE);
            }
            tvCorrectAnswer.setText(correctAnswer);
            childAt.findViewById(R.id.btn_exam_submit).setVisibility(View.GONE);
        }
    }
    public int getMode() {
        return mode;
    }

    public void setmAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }
}
