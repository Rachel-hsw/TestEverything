package com.example.pc.testeverything.Recyclerview;

import java.util.List;

/**
 * Created by PC on 2018/8/3.
 */

public class Order {
    private String orderNumber;
    private String source;
    private List<OrderDetail> orderDetailList;

    public Order(String orderNumber, String source, List<OrderDetail> orderDetailList) {
        this.orderNumber = orderNumber;
        this.source = source;
        this.orderDetailList = orderDetailList;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
