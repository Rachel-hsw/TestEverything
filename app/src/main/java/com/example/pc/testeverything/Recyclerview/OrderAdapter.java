package com.example.pc.testeverything.Recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.testeverything.R;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView rl_details;
        TextView orderNumber;
        private Order order;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNumber = (TextView) itemView.findViewById(R.id.orderNumber);
            rl_details = (RecyclerView) itemView.findViewById(R.id.rl_details);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        public void bindData(Order order, int position) {
            this.order = order;
        }

        public void setStyle() {
            orderNumber.setText("hhhhhhhhhhhhhhhh");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.orderNumber:
                    orderNumber.setText("ddddddd");
                    break;
            }
        }

        public void setListener() {
            orderNumber.setOnClickListener(this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("ddddddddddd", "dddddddd3");
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("ddddddddddd", "dddddddd4");
        Order order = orderList.get(position);
        holder.orderNumber.setText(order.getOrderNumber());
        OrderDetailAdapter adapter = new OrderDetailAdapter(context, order.getOrderDetailList());
        LinearLayoutManager linear = new LinearLayoutManager(context);
        holder.rl_details.setLayoutManager(linear);
        holder.rl_details.setAdapter(adapter);
        holder.bindData(order, position);
        holder.setListener();
    }

    /**
     * 最先调用
     *
     * @return
     */
    @Override
    public int getItemCount() {
        Log.i("ddddddddddd", "dddddddd5");
        //如果getItemCount为0，则其他两个方法都不会执行
        return orderList.size();
    }
}
