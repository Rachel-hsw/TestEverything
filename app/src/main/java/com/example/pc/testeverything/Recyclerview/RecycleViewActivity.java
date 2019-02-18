package com.example.pc.testeverything.Recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pc.testeverything.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2018/9/17.
 */

public class RecycleViewActivity extends Activity {
    private List<Order> orders = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOders();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rl_orders);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        orderAdapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(orderAdapter);
    }

    private void initOders() {
        orders.clear();
        for (int i = 0; i < 10; i++) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            orderDetails.add(new OrderDetail("名称名称名称名称名称名称名称名称名称", "x1"));
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
