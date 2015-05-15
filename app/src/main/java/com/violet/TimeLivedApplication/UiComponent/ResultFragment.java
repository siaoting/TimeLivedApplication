package com.violet.TimeLivedApplication.UiComponent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.violet.TimeLivedApplication.Data.CalcLife;
import com.violet.TimeLivedApplication.R;
import com.violet.TimeLivedApplication.Data.CustomDate;
import com.violet.TimeLivedApplication.Data.Person;
import com.violet.TimeLivedApplication.Data.Person.Gender;
import com.violet.TimeLivedApplication.Data.Person.Nation;

public class ResultFragment extends Fragment {
    public static final String TAG = InfoFragment.class.getSimpleName();

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_EXPECTANCY = "expectancy";

    private static final String KEY_GENDER = "gender";
    private static final String KEY_NATION = "nation";

    private TextView mLifeExpectancyTextView;
    private TextView mPassTextView;
    private TextView mLeftTextView;
    private CircleProgress mResultProgress;

    private CustomDate mBirthday;
    private Gender mGender;
    private Nation mNation = Nation.TAIWAN;
    private int mExpectancy;

    private CalcLife mCalcLife;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        mLifeExpectancyTextView = (TextView) view.findViewById(R.id.lifeExpectancyTextView);
        mLifeExpectancyTextView.setText(String.format(getString(R.string.life_expectancy), mCalcLife.getLifeExpectancy()));

        mPassTextView = (TextView) view.findViewById(R.id.passTextView);
        mLeftTextView = (TextView) view.findViewById(R.id.leftTextView);
        mResultProgress = (CircleProgress) view.findViewById(R.id.resultProgressBar);
        updateResult();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParameter();

        if(Person.DEFAULT_LIFE_EXPECTANCY == mExpectancy) {
            mCalcLife = new CalcLife(mBirthday,
                    mGender,
                    mNation);
        } else {
            mCalcLife = new CalcLife(mBirthday, mExpectancy);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateResult();
    }

    public void setParameter( int year,
                              int month,
                              int day,
                              int expectancy,
                              Gender gender,
                              Nation nation) {
        Bundle bundle = new Bundle();

        bundle.putInt(KEY_YEAR, year);
        bundle.putInt(KEY_MONTH, month);
        bundle.putInt(KEY_DAY, day);

        bundle.putInt(KEY_EXPECTANCY, expectancy);

        if(null != gender) {
            bundle.putInt(KEY_GENDER, gender.ordinal());
        }

        if(null != nation) {
            bundle.putInt(KEY_NATION, nation.ordinal());
        }

        this.setArguments(bundle);
    }

    public void getParameter() {
        Bundle bundle = getArguments();

        mBirthday = new CustomDate(bundle.getInt(KEY_YEAR, 0),
                bundle.getInt(KEY_MONTH, 0),
                bundle.getInt(KEY_DAY, 0));

        mExpectancy= bundle.getInt(KEY_EXPECTANCY, Person.DEFAULT_LIFE_EXPECTANCY);

        if(mExpectancy == Person.DEFAULT_LIFE_EXPECTANCY) {
            mGender = Gender.values()[bundle.getInt(KEY_GENDER, Gender.NONE.ordinal())];
            mNation = Nation.values()[bundle.getInt(KEY_NATION, Nation.NONE.ordinal())];
        }
    }

    private void updateResult() {
        int totalDate = mCalcLife.getTotalDate();
        int passDate = mCalcLife.getPassDate() ;
        int leftDate = totalDate - passDate;

        int progress;

        if(leftDate > 0) {
            mLeftTextView.setText(String.format(getString(R.string.life_left), totalDate - passDate));
            progress = passDate * 100 / totalDate;
        } else {
            mLeftTextView.setText(R.string.life_left_long);
            progress = 100;
        }

        mPassTextView.setText(String.format(getString(R.string.life_pass), passDate));
        mResultProgress.setProgress(progress);
    }
}
