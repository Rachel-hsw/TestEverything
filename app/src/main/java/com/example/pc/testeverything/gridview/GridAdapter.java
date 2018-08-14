package com.example.pc.testeverything.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc.testeverything.R;
import com.example.pc.testeverything.Recyclerview.OrderDetail;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

class GridAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    List<OrderDetail> orderDetailList;

    public GridAdapter(GridViewActivity context, List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    private class Holder implements View.OnClickListener {
        TextView detailName;
        TextView detailNumber;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.detailName:
                    setStyle();
                    break;

            }
        }

        private void setStyle() {
        }

        public void setListener() {
            detailName.setOnClickListener(this);
        }
    }

    @Override
    public int getCount() {
        return orderDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new Holder();
            holder.detailNumber = (TextView) view.findViewById(R.id.detailNumber);
            holder.detailName = (TextView) view.findViewById(R.id.detailName);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        OrderDetail orderDetail = orderDetailList.get(position);
        holder.detailName.setText(orderDetail.getDetailName());
        holder.detailNumber.setText(orderDetail.getDetailNumber());
        holder.setListener();
        return view;
    }
}
