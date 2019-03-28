package com.example.pc.testeverything.testJava;

/**
 * Created by Rachel on 2019/3/1.
 */

public class Test {
    //private类型的值，开启了子线程后，外面改变里面不变 引用类型 和值传递类型

    String str = new String("good");
    char[] ch = {'a', 'b', 'c'};

    /**
     * int是32位的二进制,当int转成byte的时候，那么计算机会只保留最后8位
     *
     * @param args
     */
    public static void main(String... args) {
        Test object = new Test();
//        object.change(object.str,object.ch);
//        System.out.println(object.str);
//        System.out.println(object.ch);
        object.change(object);
        System.out.println(object.str);
        System.out.println(object.ch);

//        int hhh=555555;
//        change(hhh);
//        System.out.println(hhh);

        int[] hhh = {555555};
        change(hhh);
        System.out.println(hhh[0]);

        byte x = 125;
        x++;
        System.out.println(x);
        while (x > 100) x++;
        System.out.println(x);
        int b = 456;//111001000
        byte test = (byte) b;//11001000  最高位为1，负数，负数在计算机中都是以补码的形式保存的，11001000的原码为00111000，转化为十进制，并加负号
        System.out.println(test);
    }

    private static void change(int[] hhh) {
        hhh[0] = 7777;
    }

    private void change(Test object) {
        object.str = "test";
        object.ch[0] = 'g';
    }


    private void change(String str, char[] ch) {
        str = "test";
        ch[0] = 'g';
    }
}
