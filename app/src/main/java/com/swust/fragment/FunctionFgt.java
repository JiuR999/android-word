package com.swust.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.swust.R;
import com.swust.activity.ExamActivity;
import com.swust.activity.WordListActivity;
import com.swust.activity.WordsActivity;
import com.swust.adapter.FuntionAdapter;
import com.swust.databinding.DialogAddWordBinding;
import com.swust.databinding.FragmentFunListBinding;
import com.swust.db.dao.WordDao;
import com.swust.factory.AnimationFactory;
import com.swust.pojo.Function;
import com.swust.pojo.User;
import com.swust.pojo.Word;
import com.swust.utils.Const;
import com.swust.utils.IntentFactory;
import com.swust.utils.OkHttpUtils;
import com.swust.utils.PlaySoundUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FunctionFgt extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private List<Function> functions;
    private FuntionAdapter funtionAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private FragmentFunListBinding binding;
    private Handler handler;
    public static FunctionFgt newInstance(User user){
        Bundle bundle = new Bundle();
        bundle.putString(Const.ACCOUNT, user.getAccount());
        bundle.putString(Const.AVATAR, user.getAvatar());
        FunctionFgt functionFgt = new FunctionFgt();
        functionFgt.setArguments(bundle);
        return functionFgt;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFunListBinding.inflate(inflater,container,false);
        initFunctions();
        initHandler();
        ListView listView = binding.list;
        binding.fbtnAddWord.setOnClickListener(this);
        binding.imgSearch.setOnClickListener(this);
        binding.btnCallapse.setOnClickListener(this);
        funtionAdapter = new FuntionAdapter(getActivity(),functions);
        listView.setAdapter(funtionAdapter);
        listView.setOnItemClickListener(this);
        return binding.getRoot();
    }

    private void initHandler() {
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int msgId = msg.what;
                if(msgId == 1 ){
                    binding.cardTrans.setVisibility(View.VISIBLE);
                    AnimationFactory.createSlideInAnimation(binding.cardTrans).start();
                    AnimationFactory.createSlideOutAnimation(binding.progressCircular).start();

                    AnimationFactory.createSlideInAnimation(binding.imgSearch).start();
                    //binding.imgSearch.setVisibility(View.VISIBLE);
                    binding.progressCircular.setVisibility(View.GONE);
                    binding.tvTrans.setText((CharSequence) msg.obj);
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaySoundUtils.playSound(getContext());
        if(position == 0){
            Intent intent = new Intent(getActivity(), WordsActivity.class);
            intent.putExtra(Const.META_KEY,"官方单词库");
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else if(position == 1){
            Intent intent = new Intent(getActivity(), WordListActivity.class);
            intent.putExtra(Const.META_KEY, "个人单词库");
            intent.putExtra(Const.ACCOUNT, getArguments().getString(Const.ACCOUNT));
            startActivity(intent);
        } else if (position == 2) {
            String[] stringArray = getActivity().getResources().getStringArray(R.array.exam_type_value);
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle(R.string.exam_title)
                    .setItems(R.array.exam_type, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), ExamActivity.class);
                            intent.putExtra(Const.META_KEY,stringArray[which]);
                            startActivity(intent);
                        }
                    }).show();

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.fbtn_add_word){
            bottomSheetDialog = new BottomSheetDialog(getActivity());
            DialogAddWordBinding binding = DialogAddWordBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(binding.getRoot());
            binding.btnAddWord.setOnClickListener(v2->{
               String wordKey =  binding.edWordKey.getText().toString();
               String wordPhono =  binding.edWordPhono.getText().toString();
               String wordTans =  binding.edWordTans.getText().toString();
               String wordExample =  binding.edExample.getText().toString();
               if(wordKey.isEmpty() || wordPhono.isEmpty() || wordTans.isEmpty() || wordExample.isEmpty()){
                   Toast.makeText(getActivity(), "待添加内容不能为空!", Toast.LENGTH_SHORT).show();
               } else {
                   String account = getArguments().getString(Const.ACCOUNT,"");
                   Log.d("FunctionFgt:",account);
                   String tableName = Const.WORDS_USER + account;
                   WordDao wordDao = new WordDao(getActivity());
                   Word word = new Word(null,wordKey,wordPhono,wordTans,wordExample,null);
                   wordDao.insertWord(tableName,word);
                   Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                   bottomSheetDialog.dismiss();
               }
            });
            binding.btnCancel.setOnClickListener(cancel->bottomSheetDialog.dismiss());
            bottomSheetDialog.show();
        } else if (id == R.id.img_search) {
            String trans = binding.funEditTrans.getText().toString();
            if("".equals(trans)){
                Toast.makeText(getActivity(), "请输入待翻译英语或汉语！", Toast.LENGTH_SHORT).show();
                return;
            }
            // 创建向右滑出屏幕的动画
            ObjectAnimator slideOutAnimation = AnimationFactory.createSlideOutAnimation(binding.imgSearch);
            // 创建从右往左进入屏幕的动画
            ObjectAnimator slideInAnimation = AnimationFactory.createSlideOutAnimation(binding.progressCircular);
            slideOutAnimation.start();
            // 设置动画监听器，在动画结束时隐藏ImageView并显示ProgressBar
            slideOutAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //binding.imgSearch.setVisibility(View.INVISIBLE);
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    slideInAnimation.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });

            new Thread(()->{
                String res = OkHttpUtils.doGet(getString(R.string.api_translate) + trans);
                Message message = new Message();
                message.what = 1;
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String text = jsonObject.getJSONObject("result")
                            .optString("targetText");
                    message.what = 1;
                    message.obj = text;
                } catch (JSONException e) {
                    message.obj = "请求失败 请重试！";
                } finally {
                    handler.sendMessage(message);
                }
            }).start();
        } else if (id == R.id.btn_callapse) {
            AnimationFactory.createSlideInAnimation(binding.imgSearch).start();
            AnimationFactory.createSlideOutAnimation(binding.cardTrans).start();
        }
    }
    private void initFunctions() {
        functions = new ArrayList<>();
        Function function1 = new Function(R.drawable.unit_left_back_primary,"官方单词大全","涵盖海量收录单词");
        Function function2 = new Function(R.drawable.icon_function_mywords,"个人单词库","个人收集记录的单词库");
        Function function3 = new Function(R.drawable.icon_function_exam,"单词自测","检验测试背诵状态");
        functions.add(function1);
        functions.add(function2);
        functions.add(function3);
    }

}
