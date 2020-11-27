package com.example.pc.testeverything.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pc.testeverything.R;
import com.example.pc.testeverything.Recyclerview.OrderDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */


public class GridViewActivity extends AppCompatActivity {
    private GridView gridview;

    GridAdapter myGridView;
    List<OrderDetail> orderDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_layout);
        findViewById(R.id.rll_view_cabinet_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GridViewActivity.this, "用户点击了", Toast.LENGTH_LONG).show();
            }
        });
    /*    gridview = (GridView) findViewById(R.id.gridview);

        myGridView = new GridAdapter(this, initOders());
        gridview.setAdapter(myGridView);*/

      /*  View gridview = findViewById(R.id.gridview);

        myGridView = new GridAdapter(this, initOders());
        gridview.setAdapter(myGridView);*/

        orderDetailList = initOders();
        Iterator iterator = orderDetailList.iterator();
        while (iterator.hasNext()) {
            OrderDetail orderDetail = (OrderDetail) iterator.next();
            if (orderDetail.getDetailNumber().equals("2")) {
                iterator.remove();
            }
        }
    }

    private List<OrderDetail> initOders() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orderDetails.add(new OrderDetail("爱上豆浆", "2"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
        }

        return orderDetails;
    }
}
