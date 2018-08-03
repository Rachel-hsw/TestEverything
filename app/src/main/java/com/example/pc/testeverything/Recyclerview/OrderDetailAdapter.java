package com.example.pc.testeverything.Recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.testeverything.R;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetailList;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.orderdetail_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        holder.detailName.setText(orderDetail.getDetailName());
        holder.detailNumber.setText(orderDetail.getDetailNumber());

    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailName;
        TextView detailNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            detailName = (TextView) itemView.findViewById(R.id.detailName);
            detailNumber = (TextView) itemView.findViewById(R.id.detailNumber);
        }
    }
}
