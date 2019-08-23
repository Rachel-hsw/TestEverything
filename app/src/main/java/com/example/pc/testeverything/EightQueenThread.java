package com.example.pc.testeverything;

import java.util.concurrent.Callable;

class EightQueenThread implements Callable<Long> {
    private short[] chess;
    private short N;

    public EightQueenThread(short[] chess) {
        this.chess = chess;
        this.N = (short) chess.length;
    }


    @Override
    public Long call() throws Exception {
        return putQueenAtRow(chess, (short) 1);
    }


    private Long putQueenAtRow(short[] chess, short row) {
        if (row == N) {
            return (long) 1;
        }

        short[] chessTemp = chess.clone();
        long sum = 0;
        /**
         * 向这一行的每一个位置尝试排放皇后
         * 然后检测状态，如果安全则继续执行递归函数摆放下一行皇后
         */
        for (short i = 0; i < N; i++) {
            //摆放这一行的皇后
            chessTemp[row] = i;

            if (isSafety(chessTemp, row, i)) {
                sum += putQueenAtRow(chessTemp, (short) (row + 1));
            }
        }

        return sum;
    }

    private static boolean isSafety(short[] chess, short row, short col) {
        //判断中上、左上、右上是否安全
        short step = 1;
        for (short i = (short) (row - 1); i >= 0; i--) {
            if (chess[i] == col)   //中上
                return false;
            if (chess[i] == col - step)  //左上
                return false;
            if (chess[i] == col + step)  //右上
                return false;

            step++;
        }

        return true;
    }
}
