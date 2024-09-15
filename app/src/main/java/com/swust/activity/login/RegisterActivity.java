package com.swust.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.swust.databinding.ActivityRegisterBinding;
import com.swust.db.UserDBOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 2;
    private String base64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.avatar.setOnClickListener(v -> {
// 检查并申请读取外部存储的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
            } else {
                // 打开相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }});
        binding.btnReg.setOnClickListener(v -> {
            String name = binding.edRegName.getText().toString();
            String account = binding.edRegAccount.getText().toString();
            String password = binding.edRegPassword.getText().toString();
            String repassword = binding.edRegRepassword.getText().toString();
            register(name, account, password, repassword);
        });
    }

    private void register(String name, String account, String password, String repassword) {
        if (password.length() < 6 || password.length() >= 10) {
            Toast.makeText(this, "密码长度过长或过短!", Toast.LENGTH_SHORT).show();
        } else if (password.equals("") || name.equals("")) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (isContainChinese(password) || isContainChinese(account)) {
            Toast.makeText(this, "密码或密码不能包含中文!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repassword)) {
            Toast.makeText(this, "两次密码输入不一致，请检查后重试！", Toast.LENGTH_SHORT).show();
        } else {
            if (!binding.cbAgree.isChecked()) {
                Toast.makeText(this, "请先同意相关协议!", Toast.LENGTH_SHORT).show();
            } else {
                SQLiteOpenHelper instance = UserDBOpenHelper.getInstance(this);
                SQLiteDatabase wdb = instance.getWritableDatabase();
                String sql = "select * from users where _account = ?";
                Cursor cursor = wdb.rawQuery(sql, new String[]{account});
                if(cursor.getCount() != 0){
                    Toast.makeText(this, "该用户已经被注册!", Toast.LENGTH_SHORT).show();
                }else {
                    if(wdb.isOpen()){
                        String sql1 = "insert into users(_account,_password,_name,_avatar) values(?,?,?,?)";
                        wdb.execSQL(sql1,new Object[]{account,password,name,base64});
                        wdb.close();
                        Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                /*SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                        , LoginActivity.USER, account, password);
                SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                        , LoginActivity.USER, LoginActivity.PASSWORD, password);
                SharedPreferenceUtil.save(this, SharedPreferenceUtil.Type.STRING
                        , LoginActivity.USER, account+LoginActivity.NAME,name);*/

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            // 使用 imageUri 进行后续处理
// 将 Uri 转化为字节数组
            byte[] imageBytes;
            try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.avatar.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                base64 = Base64.encodeToString(byteArray,Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}