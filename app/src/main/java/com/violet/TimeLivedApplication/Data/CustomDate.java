package com.violet.TimeLivedApplication.Data;

public class CustomDate {
    private int mYear;
    private int mMonth;
    private int mDay;

    public CustomDate(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }
}
