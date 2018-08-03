package com.example.pc.testeverything.Recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc.testeverything.R;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class OrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rl_details;
        TextView orderNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNumber = (TextView) itemView.findViewById(R.id.orderNumber);
            rl_details = (RecyclerView) itemView.findViewById(R.id.rl_details);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        ((ViewHolder) holder).orderNumber.setText(order.getOrderNumber());
        OrderDetailAdapter adapter = new OrderDetailAdapter(context, order.getOrderDetailList());
        LinearLayoutManager linear = new LinearLayoutManager(context);
        ((ViewHolder) holder).rl_details.setLayoutManager(linear);
        ((ViewHolder) holder).rl_details.setAdapter(adapter);

    }


    @Override
    public int getItemCount() {
        //如果getItemCount为0，则其他两个方法都不会执行
        return orderList.size();
    }
}
