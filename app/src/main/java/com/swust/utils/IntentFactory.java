package com.swust.utils;

import android.content.Context;
import android.content.Intent;

import com.swust.SoundService;

public class IntentFactory {
    public static Intent createSoundIntent(Context context){
        Intent intent = new Intent(context, SoundService.class);
        intent.setAction("PLAY_SOUND");
        return intent;
    }
}
