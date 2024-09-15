package com.swust.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class PlaySoundUtils {
    public static void playSound(Context context){
        Intent soundIntent = IntentFactory.createSoundIntent(context);
        context.startService(soundIntent);
    }
}
