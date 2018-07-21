package com.example.pc.testeverything.Activity.main;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.pc.testeverything.R;

public class SoundPoolActivity extends Activity {

    Button playButton;
    SoundPool sp;
    int soundID_1;
    int streamID_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_layout);

        sp=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundID_1=sp.load(getApplicationContext(), R.raw.dingdong, 1);

        playButton=(Button)findViewById(R.id.play);
        SpeackUtils.getInstance().play();
        playButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                streamID_1=sp.play(soundID_1, 0.8f, 0.8f,1, 1, 1.0f);
            }
        });
    }
}
