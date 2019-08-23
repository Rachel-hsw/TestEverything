package com.example.pc.testeverything;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class EightQueen7 {
    private static final short K = 8;     //使用常量来定义，方便之后解N皇后问题
    private static short N = 0;

    public static void main(String[] args) throws Exception {
        for (N = 13; N <= 15; N++) {
            long count = 0;
            Date begin = new Date();
            /**
             * 初始化棋盘，使用一维数组存放棋盘信息
             * chess[n]=X:表示第n行X列有一个皇后
             */

            List<short[]> chessList = new ArrayList<short[]>(N);
            for (short i = 0; i < N; i++) {
                short chess[] = new short[N];
                chess[0] = i;
                chessList.add(chess);
            }

            short taskSize = (short) (N / 2 + (N % 2 == 1 ? 1 : 0));
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(taskSize);
            // 创建多个有返回值的任务
            List<Future<Long>> futureList = new ArrayList<Future<Long>>(taskSize);
            for (int i = 0; i < taskSize; i++) {
                Callable<Long> c = new EightQueenThread(chessList.get(i));
                // 执行任务并获取Future对象
                Future<Long> f = pool.submit(c);
                futureList.add(f);
            }
            // 关闭线程池
            pool.shutdown();

            for (short i = 0; i < (short) (taskSize - (N % 2 == 1 ? 1 : 0)); i++) {
                count += futureList.get(i).get();
            }
            count = count * 2;
            if (N % 2 == 1)
                count += futureList.get(N / 2).get();

            Date end = new Date();
            System.out.println("解决 " + N + "皇后问题，用时：" + String.valueOf(end.getTime() - begin.getTime()) + "毫秒，计算结果：" + count);
        }
    }


}
