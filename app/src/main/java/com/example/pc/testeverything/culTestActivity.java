package com.example.pc.testeverything;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by PC on 2018/7/19.
 */

public class culTestActivity extends Activity implements View.OnClickListener {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button plus;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button mius;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button equal;
    private Button ac;
    private Button dot;
    private Button button0;
    private Button chu;
    private EditText editText1;
    private EditText editText2;
    private double curval;
    private double leftvalue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_cul_layout);
        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        plus = (Button) findViewById(R.id.plus);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        mius = (Button) findViewById(R.id.mius);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        equal = (Button) findViewById(R.id.equal);
        ac = (Button) findViewById(R.id.ac);
        dot = (Button) findViewById(R.id.dot);
        button0 = (Button) findViewById(R.id.button0);
        chu = (Button) findViewById(R.id.chu);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        plus.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        mius.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        equal.setOnClickListener(this);
        ac.setOnClickListener(this);
        dot.setOnClickListener(this);
        button0.setOnClickListener(this);
        chu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dot:

                break;
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
                break;
            case R.id.mius:
                break;
            case R.id.plus:
                break;
            case R.id.equal:

                break;
            case R.id.ac:

                break;
            case R.id.button0:

                break;
            case R.id.chu:

                break;
        }
    }

    private void submit() {
        // validate
        String editText1String = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(editText1String)) {
            Toast.makeText(this, "editText1String不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String editText2String = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(editText2String)) {
            Toast.makeText(this, "editText2String不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
