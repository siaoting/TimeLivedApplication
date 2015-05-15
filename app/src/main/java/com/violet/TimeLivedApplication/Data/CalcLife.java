package com.violet.TimeLivedApplication.Data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.violet.TimeLivedApplication.Data.Person.Gender;
import com.violet.TimeLivedApplication.Data.Person.Nation;

public class CalcLife {
    private static final long SECONDS_IN_DAY = 60 * 60 * 24 * 1000;

    private static final HashMap<Person.Nation, Integer> sMaleLifeExpectancyMap = new HashMap();
    private static final HashMap<Person.Nation, Integer> sFemaleLifeExpectancyMap = new HashMap();

    private final int  mLifeExpectancy;
    private Calendar mBirthCalender;
    private Calendar mDeathCalender;

    private int mTotalDays;

    private CustomDate mBirthday;
    private Nation mNation;

    static {
        //Male
        sMaleLifeExpectancyMap.put(Nation.TAIWAN, 76);

        //Female
        sFemaleLifeExpectancyMap.put(Nation.TAIWAN, 82);
    }

    public CalcLife(CustomDate date,
                    Gender gender,
                    Nation nation) {
        mBirthday = date;
        mNation = nation;
        if(gender == Gender.MALE) {
            mLifeExpectancy = sMaleLifeExpectancyMap.get(mNation);
        } else {
            mLifeExpectancy = sFemaleLifeExpectancyMap.get(mNation);
        }

        calcDate();
    }

    public CalcLife(CustomDate date,
                    int expectancy) {
        mBirthday = date;
        mLifeExpectancy = expectancy;

        calcDate();
    }

    public static int getDefaultLifeExpectancy(Gender gender,
                                               Nation nation) {
        if(gender == Gender.MALE) {
            return sMaleLifeExpectancyMap.get(nation);
        } else {
            return sFemaleLifeExpectancyMap.get(nation);
        }
    }

    private void calcDate() {
        mBirthCalender = new GregorianCalendar(mBirthday.getYear(),
                mBirthday.getMonth(),
                mBirthday.getDay());
        mDeathCalender = new GregorianCalendar(mBirthday.getYear() + mLifeExpectancy,
                mBirthday.getMonth(),
                mBirthday.getDay());

        mTotalDays = (int)((mDeathCalender.getTimeInMillis() - mBirthCalender.getTimeInMillis()) / SECONDS_IN_DAY);
    }

    public int getTotalDate() {
        return mTotalDays;
    }

    public int getPassDate() {
        final Calendar curCalender = new GregorianCalendar();
        long passDays = (curCalender.getTimeInMillis() - mBirthCalender.getTimeInMillis()) / SECONDS_IN_DAY;

        return (int)passDays;
    }

    public int getLifeExpectancy() {
        return mLifeExpectancy;
    }
}
