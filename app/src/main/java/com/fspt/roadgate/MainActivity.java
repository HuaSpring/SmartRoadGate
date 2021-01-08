package com.fspt.roadgate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fspt.roadgate.manager.TTSManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnTTS, btnAnim, btnGateSystem;
    ImageView ivAnim;
    String[] words = {"hello,Hello,Hello", "Spring,Spring,Spring", "Apple,Apple,Apple", "Dog,Dog,Dog"};
    private TTSManager ttsManager;


    boolean bAnimUp = true;
    private Animation animRotateUp;// 声明两个动画对象变量
    private Animation animRotateDown;// 声明两个动画对象变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ttsManager = new TTSManager(MainActivity.this);
        btnTTS = findViewById(R.id.btn_tts);
        btnTTS.setOnClickListener(this);
        btnAnim = findViewById(R.id.btn_anim);
        btnAnim.setOnClickListener(this);
        btnGateSystem = findViewById(R.id.btn_road_system);
        btnGateSystem.setOnClickListener(this);
        ivAnim = findViewById(R.id.iv_anim);
        // Anim
        animRotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_up);
        animRotateDown = AnimationUtils.loadAnimation(this, R.anim.rotate_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tts:
                String word = words[new Random().nextInt(words.length)];
                Log.e("HHHH", "TTS Speak word :　" + word);
                ttsManager.speak(word);
                break;
            case R.id.btn_anim:
                ivAnim.startAnimation(bAnimUp ? animRotateUp : animRotateDown);
                bAnimUp = !bAnimUp;
                break;
            case R.id.btn_road_system:
                startActivity(new Intent(MainActivity.this, RoadGateSystemActivity.class));
                break;
        }
    }
}