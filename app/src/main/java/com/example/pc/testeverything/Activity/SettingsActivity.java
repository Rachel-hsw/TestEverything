package com.example.pc.testeverything.Activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.pc.testeverything.R;

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,
                        SettingActivity.class));
            }
        });

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * 显示当前设置
     */
    private void refresh() {
        boolean booleanValue = PreferenceManager.getDefaultSharedPreferences(
                this).getBoolean("boolean_value", false);
        String stringValue = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("string_value", "");
        ((TextView) findViewById(R.id.booleantextView)).setText(String
                .valueOf(booleanValue));
        ((TextView) findViewById(R.id.textTextView)).setText(stringValue);
    }
}