package com.swust.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.swust.R;
import com.swust.activity.MainActivity;
import com.swust.databinding.ActivityLoginBinding;
import com.swust.db.UserDBOpenHelper;
import com.swust.utils.Const;
import com.swust.utils.SharedPreferenceUtil;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USER = "user";
    public static final String IS_REMBER = "isRember";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String NAME = "username";
    ActivityLoginBinding loginBinding;
    boolean isPwdVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        checkIsRememberPassword();
        initRegister();
        registerClick();
    }

    private void initRegister() {
        SpannableString spannableString = new SpannableString(loginBinding.tvLoginRegister.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            //重写该方法去掉下划线
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(true);
            }
        };
        //设置文字的点击事件
        spannableString.setSpan(clickableSpan, 6, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        int color = ContextCompat.getColor(LoginActivity.this, R.color.md_theme_primary);
        spannableString.setSpan(new ForegroundColorSpan(color), 6, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        loginBinding.tvLoginRegister.setMovementMethod(LinkMovementMethod.getInstance());
        loginBinding.tvLoginRegister.setText(spannableString);
    }

    private void checkIsRememberPassword() {
        boolean isRember = (boolean) SharedPreferenceUtil.readData(this, SharedPreferenceUtil.Type.BOOLEAN
                , USER, IS_REMBER);
        if (isRember) {
            String account = (String) SharedPreferenceUtil.readData(this, SharedPreferenceUtil.Type.STRING
                    , USER, ACCOUNT);
            String password = (String) SharedPreferenceUtil.readData(this, SharedPreferenceUtil.Type.STRING
                    , USER, PASSWORD);
            loginBinding.cbRememberPassword.setChecked(true);
            loginBinding.editUsername.setText("");
            loginBinding.editUsername.setText(account);
            loginBinding.editPassword.setText("");
            loginBinding.editPassword.setText(password);
        }
    }

    private void registerClick() {
        loginBinding.btnLogin.setOnClickListener(this);
        loginBinding.imgPwdVisible.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            login();
        } else if (id == R.id.img_pwd_visible) {

            if (isPwdVisible) {
                //从密码可见模式变为密码不可见模式
                loginBinding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                loginBinding.imgPwdVisible.setBackground(getDrawable(R.drawable.twotone_visibility_off_24));
            } else {
                loginBinding.editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                loginBinding.imgPwdVisible.setBackground(getDrawable(R.drawable.twotone_visibility_24));
            }
            loginBinding.editPassword.setSelection(loginBinding.editPassword.getText().length());
            isPwdVisible = !isPwdVisible;
        }
    }


    private void login() {
        SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.BOOLEAN
                , USER, IS_REMBER, loginBinding.cbRememberPassword.isChecked());
        String account = loginBinding.editUsername.getText().toString();
        String password = loginBinding.editPassword.getText().toString();
        if (account.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() >= 12 || password.length() < 6) {
                Toast.makeText(this, "密码过长或过短!", Toast.LENGTH_SHORT).show();
            } else {
                SQLiteOpenHelper instance = UserDBOpenHelper.getInstance(this);
                SQLiteDatabase database = instance.getReadableDatabase();
                if(database.isOpen()){
                    String sql = "select * from users where _account = ?";
                    Cursor cursor = database.rawQuery(sql, new String[]{account});
                    if(cursor.getCount() > 0){
                        cursor.moveToFirst();
                        String stdPwd = cursor.getString(cursor.getColumnIndex(Const.PASSWORD));
                        if(password.equals(stdPwd)){
                            String avatar = cursor.getString(cursor.getColumnIndex(Const.AVATAR));
                            String name = cursor.getString(cursor.getColumnIndex(Const.NAME));
                            SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                                    , USER, ACCOUNT, account);
                            SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                                    , USER, PASSWORD, password);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING,
                                    Const.AVATAR,Const.AVATAR,avatar);
                            intent.putExtra(Const.NAME,name);
                            intent.putExtra(Const.PASSWORD,password);
                            intent.putExtra(Const.ACCOUNT,account);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "密码输入有误 请检查后重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "该账号未注册!", Toast.LENGTH_SHORT).show();
                    }
                }
/*                String stdPassword = (String) SharedPreferenceUtil.readData(this, SharedPreferenceUtil.Type.STRING
                        , USER, account);
                if(stdPassword.equals("")){
                    Toast.makeText(this, "该账号未注册!", Toast.LENGTH_SHORT).show();
                }else if (stdPassword.equals(password)) {
                    SQLiteOpenHelper instance = MySQLiteOpenHelper.getInstance(this);
                    SQLiteDatabase database = instance.getReadableDatabase();
                    if(database.isOpen()){
                        String sql = "select * from users where _account = ? and _password = ?";
                        Cursor cursor = database.rawQuery(sql, new String[]{account, password});
                        if(cursor.getCount() > 0){
                            SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                                    , USER, ACCOUNT, account);
                            SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                                    , USER, PASSWORD, password);
                            startActivity(new Intent(LoginActivity.this, CalculatorActivity.class));
                        }
                    }


                } else {
                    Toast.makeText(this, "账号密码错误!", Toast.LENGTH_SHORT).show();
                }*/
            }

        }
    }
}