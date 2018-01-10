package com.example.a6100890.myapplication.model;

/**
 * 存储一个字符及其出现的次数
 */
public class Data implements Comparable<Data>{
    private char c = 0;
    private int frequency = 0;

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Data [c=" + c + ", frequency=" + frequency + "]";
    }


    /**
     * 比较Data的frequency
     * @param o
     * @return
     */
    @Override
    public int compareTo(Data o) {
        if (this.frequency < o.getFrequency()) {
            return -1;
        } else if (this.frequency > o.getFrequency()) {
            return 1;
        } else {
            return 0;
        }
    }
}
