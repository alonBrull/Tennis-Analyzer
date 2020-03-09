package com.example.tennisanalyzer;

import java.util.Comparator;

public class Session {
    private String date, length;
    private long swingCount, swingAverage, swingMax;

    public Session(String date, Object swingCount, Object swingAverage, Object swingMax, Object length) {
        this.date = date;
        this.swingCount = (long) swingCount;
        this.swingAverage = (long) swingAverage;
        this.swingMax = (long) swingMax;
        this.length = (String) length;
    }


    public String getDate() {
        return date;
    }

    public long getSwingCount() {
        return swingCount;
    }

    public long getSwingAverage() {
        return swingAverage;
    }

    public double getSwingMax() {
        return swingMax;
    }

    public String getLength() {
        return length;
    }

    public String toString(){
        return date + " " + swingCount + " " + swingAverage + " " + swingMax;
    }
}
