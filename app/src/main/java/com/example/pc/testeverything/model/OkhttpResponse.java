package com.example.pc.testeverything.model;

/**
 * Created by PC on 2018/7/13.
 */

public class OkhttpResponse {

    /**
     * code : 2000
     * msg : 签名数据校验失败，为非法数据。9b162dd5262ce5465fa4fa82b86975b01eeab84d
     * timestamp : 1531475458276
     * sign : 2b5cf5f6e104b1c00d3fd9649493121830912421
     */

    private int code;
    private String msg;
    private long timestamp;
    private String sign;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
