package com.example.pc.testeverything.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.example.pc.testeverything.R;

/**
 * https://www.cnblogs.com/kevincode/p/3824421.html
 * 那么当一个Preference控件实现这两个接口时，当被点击或者值发生改变时，触发方法是如何执行的呢?事实上，它的触发规则如下：
 * <p>
 * 1 先调用onPreferenceClick()方法，如果该方法返回true，则不再调用onPreferenceTreeClick方法 ；
 * <p>
 * 如果onPreferenceClick方法返回false，则继续调用onPreferenceTreeClick方法。
 * <p>
 * 2 onPreferenceChange的方法独立与其他两种方法的运行。也就是说，它总是会运行。
 * <p>
 * <p>
 * 补充：点击某个Preference控件后，会先回调onPreferenceChange()方法，即是否保存值，然后再回
 * 调onPreferenceClick以及onPreferenceTreeClick()方法，因此在onPreferenceClick/onPreferenceTreeClick
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private SharedPreferences prefs;
    private EditTextPreference string_value_editPreference;
    private CheckBoxPreference boolean_value_Preference;
    private String TAG = "hswtest";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        string_value_editPreference = (EditTextPreference) findPreference("string_value");
        boolean_value_Preference = (CheckBoxPreference) findPreference("boolean_value");
        string_value_editPreference.setOnPreferenceChangeListener(this);
        string_value_editPreference.setOnPreferenceClickListener(this);
        boolean_value_Preference.setOnPreferenceChangeListener(this);
        boolean_value_Preference.setOnPreferenceClickListener(this);


    }

    /**
     * 当Preference的值发生改变时触发该事件，true则以新值更新控件的状态，false则do noting
     *
     * @param preference
     * @param objValue
     * @return true  代表将新值写入sharedPreference文件中。
     * false 则不将新值写入sharedPreference文件
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference.getKey().equals("string_value")) {

        }
        if (true) {
            prefs.edit().putString("www", prefs.getString("string_value", "")).apply();
        }
        Log.i(TAG, "onPreferenceChange:new Value = " + String.valueOf(objValue));
        return true;
    }

    /**
     * 当点击控件时触发发生，可以做相应操作。
     * getEditText()  返回的是我们在该控件中输入的文本框值           getText()返回的是我们之前sharedPreferen文件保存的值
     *
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (!prefs.getString("www", "").equals("")) {
            String stringValue = prefs.getString("www", "");
            Log.i(TAG, "onPreferenceClick www:" + stringValue);
            string_value_editPreference.getEditText().setText(stringValue);
            prefs.edit().putString("string_value", stringValue).apply();
            Log.i(TAG, "onPreferenceClick:Old Value=" + string_value_editPreference.getText() + ", New Value=" + string_value_editPreference.getEditText().getText().toString());
        }
        return false;
    }

    /**
     * 当Preference控件被点击时，触发该方法。
     *
     * @param preferenceScreen
     * @param preference       preference   点击的对象。
     * @return true  代表点击事件已成功捕捉，无须执行默认动作或者返回上层调用链。 例如，不跳转至默认Intent。
     * false 代表执行默认动作并且返回上层调用链。例如，跳转至默认Intent。
     */
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.i(TAG, "onPreferenceTreeClick:Old Value=" + string_value_editPreference.getText() + ", New Value=" + string_value_editPreference.getEditText().getText().toString());

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
