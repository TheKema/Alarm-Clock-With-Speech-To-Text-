package com.example.ainullov.kamil.alarmclock;

public class Product{
    private String name;
    private int checkBoxRes;
    private boolean onOrOff;
    private long timeInMillis;

    Product(String name, long timeInMillis, int checkBoxRes, boolean onOrOff){
        this.name = name;
        this.timeInMillis = timeInMillis;
        this.checkBoxRes = checkBoxRes;
        this.onOrOff = onOrOff;
    }

    public boolean isOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(boolean onOrOff) {
        this.onOrOff = onOrOff;
    }

    public int getCheckBoxRes() {
        return checkBoxRes;
    }

    public void setCheckBoxRes(int checkBoxRes) {
        this.checkBoxRes = checkBoxRes;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}