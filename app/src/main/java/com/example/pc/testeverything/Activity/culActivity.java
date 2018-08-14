package com.example.pc.testeverything.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.testeverything.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 目前还有超级多问题
 */
public class culActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5,
            btn_6, btn_7, btn_8, btn_9, btn_equal,
            btn_point, btn_clean, btn_del, btn_add,
            btn_subtract, btn_multiply, btn_divide;
    EditText et_result;
    EditText et_cul;
    boolean clear_flag;
    BigDecimal leftVal = new BigDecimal("0");
    BigDecimal culVal = new BigDecimal("0");
    StringBuilder culValString = new StringBuilder("0");
    StringBuilder resultSting = new StringBuilder("0");
    String method = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cul_layout_hh);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_clean = (Button) findViewById(R.id.btn_clean);
        btn_equal = (Button) findViewById(R.id.btn_equal);
        btn_subtract = (Button) findViewById(R.id.btn_subtract);
        btn_multiply = (Button) findViewById(R.id.btn_multiplay);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_point = (Button) findViewById(R.id.btn_point);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_divide = (Button) findViewById(R.id.btn_divide);
        et_cul = (EditText) findViewById(R.id.et_input);
        et_result = (EditText) findViewById(R.id.et_result);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_subtract.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        et_cul.setOnClickListener(this);
        et_result.setOnClickListener(this);
        et_cul.setText("0");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        String str = et_cul.getText().toString();
        switch (v.getId()) {
            case R.id.btn_point:
                if (!culValString.toString().contains(".")) {
                    culValString = culValString.append(((Button) v).getText().toString().toString());
                    resultSting = resultSting.append(((Button) v).getText().toString());
                    et_cul.setText(culValString);
                    et_result.setText(resultSting);

                }
                break;
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (culValString.toString().startsWith("0") && !culValString.toString().contains(".")) {
                    resultSting = new StringBuilder("");
                    culValString = new StringBuilder("");

                }
                resultSting = resultSting.append(((Button) v).getText().toString());
                culValString = culValString.append(((Button) v).getText().toString());
                culVal = new BigDecimal(culValString.toString());
                et_cul.setText(culValString);
                et_result.setText(resultSting);
                break;
            case R.id.btn_add:
            case R.id.btn_subtract:
            case R.id.btn_multiplay:
            case R.id.btn_divide:
                switch (((Button) v).getText().toString()) {
                    case "+":
                        method = "add";
                        break;
                    case "-":
                        method = "subtract";
                        break;
                    case "x":
                        method = "multiplay";
                        break;
                    case "/":
                        method = "divide";
                        break;
                }
                culValString = new StringBuilder("");
                if (!resultSting.toString().contains("+")&&!resultSting.toString().contains("x")&&!resultSting.toString().contains("-")&&!resultSting.toString().contains("/")&&resultSting.lastIndexOf("+") != resultSting.length() - 1&&resultSting.lastIndexOf("-")!= resultSting.length() - 1&&resultSting.lastIndexOf("x") != resultSting.length() - 1&&resultSting.lastIndexOf("/")!= resultSting.length() - 1) {
                    resultSting = resultSting.append(((Button) v).getText().toString());
                    leftVal = culVal;
                    culVal=new BigDecimal("0");
                    et_result.setText(resultSting);
                }
                break;
            case R.id.btn_clean:
                clearCount();
                et_cul.setText(culValString);
                et_result.setText(resultSting);
                break;
            case R.id.btn_del:
                if (culValString.toString().length()==1){
                    culValString=new StringBuilder("0");
                }else if (culValString.toString().length()>1){
                    culValString=new StringBuilder(culValString.toString().substring(0,culValString.toString().length()-1));

                }
                et_cul.setText(culValString);
                break;
            case R.id.btn_equal:
                if (leftVal.toString().equals("0")){
                    et_cul.setText(culValString);
                    break;
                }
                if (culVal.toString().equals("0")){
                    et_cul.setText("错误的输入");
                    clearCount();
                    break;
                }
                //直接用Double类型计算，会有精度问题
                BigDecimal result = null;
                switch (method) {
                    case "add":
                        result =leftVal.add(culVal);
                        break;
                    case "subtract":
                        result =leftVal.subtract(culVal);
                        break;
                    case "multiplay":
                        result =leftVal.multiply(culVal);
                        break;
                    case "divide":
                        if (culVal.toString().equals("0")){
                            break;
                        }
                        result =leftVal.divide(culVal,15, RoundingMode.HALF_UP);
                        break;
                }
                if (culVal.toString().equals("0")){
                    et_cul.setText("错误的输入");
                }else{
                    culValString = new StringBuilder(String.valueOf(result).replaceAll("0+$", "").replaceAll("[.]$", ""));
                    et_cul.setText(culValString);
                }
                culVal =result;
                leftVal = new BigDecimal("0");
                method = "";
                resultSting = new StringBuilder("0");
                culValString = new StringBuilder("0");
                break;
            default:
                break;
        }
    }

    private void clearCount() {
        leftVal = new BigDecimal("0");
        culVal = new BigDecimal("0");
        method = "";
        resultSting = new StringBuilder("0");
        culValString = new StringBuilder("0");
    }
}
