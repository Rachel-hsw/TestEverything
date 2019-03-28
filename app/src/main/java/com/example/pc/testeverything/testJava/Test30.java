package com.example.pc.testeverything.testJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Rachel on 2019/3/25.
 */

public class Test30 {
    public static void main(String[] args) {
        try {
            System.out.println(new File(".").getAbsolutePath());//E:\Hmygit\look\TestEverything\.
            FileInputStream fis = new FileInputStream("Test30.java");
            InputStreamReader dis = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(dis);
            String s;
            while ((s = reader.readLine()) != null) {
                System.out.println("read:" + s);
            }
            dis.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
