package com.example.pc.testeverything.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pc.testeverything.Activity.main.Fruit;
import com.example.pc.testeverything.Activity.main.FruitAdapter;
import com.example.pc.testeverything.DialogActivity;
import com.example.pc.testeverything.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 主要的activity
 * 当弹出加载框时，由于加载框是小小的一只，所以MainActivity仍处于可见状态，但是是在暂停状态，puase
 * 即说明当Activity处于暂停状态时，子线程仍可以修改当前界面
 * 当界面处于暂停状态时，无法接受触屏点击等事件
 */

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private int i = 0;
    private Button button;
    private Fruit[] fruits = {new Fruit("弹出二维码", R.mipmap.fox), new Fruit("约束布局", R.mipmap.fox),
            new Fruit("下单", R.mipmap.fox),new Fruit("碎片", R.mipmap.fox),
            new Fruit("Apple", R.mipmap.fox),new Fruit("Apple", R.mipmap.fox),
            new Fruit("Apple", R.mipmap.fox),new Fruit("Apple", R.mipmap.fox)
    };

    private List<Fruit> fruitList = new ArrayList<>();

    private FruitAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //开启子线程，验证子线程是否可以更新已经处于Pause状态的activity
      /*  updateTextAtThread();*/
        Log.i("testhsw", "主线程1");
        //弹出加载框
        showDialog();
        button.setText("点击我啊");
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        long i=229;
        float w=(float) 229/100;
        double e=6.5558888888888885;

        float r =3.777f;
        int a=7;
        int b=6;
        DecimalFormat df=new DecimalFormat("0.00");

        System.out.println(df.format((float)a/b));
        String s = "10001.0110000";
        if(s.indexOf(".") > 0){
            //正则表达
            s = s.replaceAll("0+$", "");//去掉后面无用的零
          /*  s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点*/
        }
        System.out.println(s);
        Math.sqrt(9);
        Double d = new Double("1234567890.12");
        System.out.println("d:="+d);//d:1.23456789012E9
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        System.out.println("d:="+nf.format(d));//nf.format(d):1234567890.12
        double u=9;
        System.out.println("d:="+u);//nf.format(d):1234567890.12

    }
    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i <fruits.length; i++) {
            fruitList.add(fruits[i]);
        }
    }
    private void initView() {
        //隐藏android自带的标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        text = (TextView) findViewById(R.id.mymytext);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点我干嘛煞笔", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("testhsw", "onpuse");
    }

    public void showDialog() {
        Intent i = new Intent(this, DialogActivity.class);
        startActivity(i);
    }
    public void updateTextAtThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    Log.i("testhsw", "子线程1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("testhsw", "主线程2");
                      /*  Toast.makeText(MainActivity.this, "hah"+i, Toast.LENGTH_SHORT).show();*/
                            text = (TextView) findViewById(R.id.mymytext);
                            text.setText(String.valueOf(i));
                        }
                    });
                }
            }
        }).start();
    }
}