package com.example.pc.testeverything.otg;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * readLine方法
 * 功能：读取一个文本行。通过下列字符之一即可认为某行已终止：换行 ('\n')、回车 ('\r') 或回车后直接跟着换行。
 */
public class javaOther {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                int n = Integer.parseInt(line);
                int[] price = new int[n];
                int[] result = new int[n];
                line = br.readLine();
                String[] str = line.split(" ");
                for (int i = 0; i < n; i++) {
                    price[i] = Integer.parseInt(str[i]);
                }
                for (int j = 0; j < price.length; j++) {
                    if (j == 0) result[j] = (price[j] + price[j + 1]) / 2;
                    else if (j == n - 1) result[j] = (price[j] + price[j - 1]) / 2;
                    else result[j] = (price[j - 1] + price[j] + price[j + 1]) / 3;
                    System.out.print(result[j] + " ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}