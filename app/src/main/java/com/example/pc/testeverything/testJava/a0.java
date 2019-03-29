package com.example.pc.testeverything.testJava;

/**
 * Created by Rachel on 2019/3/29.
 */
class StuInfo {
    int xh;
    String xm;

    public StuInfo(int xh, String xm) {
        super();
        this.xh = xh;
        this.xm = xm;
    }

}

class a0 {
    public static void main(String[] args) {
        StuInfo1 刘敏 = null;
        刘敏 = new StuInfo1(10001, "whw");
        StuInfo1 王宏伟 = new StuInfo1(10002, "whw");
        System.out.println(刘敏.xm == 王宏伟.xm);
    }
}

