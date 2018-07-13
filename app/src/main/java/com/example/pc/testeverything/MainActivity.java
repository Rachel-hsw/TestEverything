package com.example.pc.testeverything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by PC on 2018/5/30.
 */

public class MainActivity extends AppCompatActivity {
private TextView text;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDialog();
        text= (TextView) findViewById(R.id.mymytext);
        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(MainActivity.this,"点我干嘛煞笔",Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("testhsw","主线程1");
        new Thread(new Runnable() {
            @Override
            public void run() {
           while (true){
                try {
                    Thread.sleep(1000*3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             i++;
               Log.i("testhsw","子线程1");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("testhsw","主线程2");
                      /*  Toast.makeText(MainActivity.this, "hah"+i, Toast.LENGTH_SHORT).show();*/
                        text= (TextView) findViewById(R.id.mymytext);
                        text.setText(String.valueOf(i));
                    }
                });



            }}
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("testhsw","onpuse");
    }

    public void showDialog(){
        Intent i = new Intent(this,ActivityDialog.class);
        startActivity(i);
    }


}