package com.fspt.roadgate.manager;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

/**
 * 操作TTS语音的类
 */
public class TTSManager {

    private TextToSpeech tts;

    public TTSManager(Context context) {
        initTTS(context);
    }

    //初始化TTS
    private void initTTS(Context context) {
        tts = new TextToSpeech(context, new OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    Log.e("TTSManager", "TTS初始化完毕");
                }
            }
        });
    }

    //读字符
    public void speak(String str) {
        tts.speak(str, TextToSpeech.QUEUE_ADD, null);
    }

    //关闭TTS语音并释放资源
    public void stopTTS() {
        if (tts != null) {
            //关闭TTS语音
            tts.stop();
            //释放不用的TTS资源
            tts.shutdown();
        }
    }
}
