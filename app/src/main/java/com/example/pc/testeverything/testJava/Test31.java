package com.example.pc.testeverything.testJava;

/**
 * Created by Rachel on 2019/3/28.
 */
class lm {
    int a;
    int b;

    public lm() {
        super();
    }

    public lm(int a, int b) {
        super();
        this.a = a;
        this.b = b;
    }
}

class Test31 {
    static void swap(int a, int b) {
        int c = a + 1;
        a = b + 1;
        b = c;

    }

    static void swap() {

    }

    public static void main(String[] args) {
        lm lma = new lm();
        lm lmb = new lm();
        swap(lma.a, lma.b);
        System.out.println(lma.a + "" + lma.b);
    }
}
