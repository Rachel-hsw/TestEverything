package com.example.pc.testeverything.Recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.pc.testeverything.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class RecycleViewTwoActivity extends Activity {
    private List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_layout);
        initOders();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rl_orders);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(orderAdapter);
    }

    private void initOders() {
        orders.clear();
        for (int i = 0; i < 10; i++) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            orderDetails.add(new OrderDetail("爱上豆浆", "x1"));
            Order order = new Order("单号：" + (i + 1), "外卖", orderDetails);
            orders.add(order);
        }
    }

}
