package com.swust.utils;

import android.content.Context;


import com.swust.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtils {
    private FileUtils() {
    }

    public static void writeData(Context context) {
        String dbPath = context.getDir(Const.DB_DIR, Context.MODE_PRIVATE) + File.separator + Const.DB_NAME;
        //String dbPath = "/data/data/" + context.getPackageName() + File.separator + Const.DB_DIR + File.separator + Const.DADABASE_WORD;
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.words);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(dbFile);
                int len;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
