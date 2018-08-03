package com.example.pc.testeverything.Recyclerview;

/**
 * Created by PC on 2018/8/3.
 */

public class OrderDetail {
    private String detailName;
    private String detailNumber;

    public OrderDetail(String detailName, String detailNumber) {
        this.detailName = detailName;
        this.detailNumber = detailNumber;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailNumber() {
        return detailNumber;
    }

    public void setDetailNumber(String detailNumber) {
        this.detailNumber = detailNumber;
    }
}
