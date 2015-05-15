package com.violet.TimeLivedApplication.UiComponent;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.violet.TimeLivedApplication.Data.CalcLife;
import com.violet.TimeLivedApplication.MainPreference;
import com.violet.TimeLivedApplication.R;
import com.violet.TimeLivedApplication.Data.Person;
import com.violet.TimeLivedApplication.Data.Person.Gender;
import com.violet.TimeLivedApplication.Data.Person.Nation;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class InfoFragment extends Fragment
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    public static final String TAG = InfoFragment.class.getSimpleName();

    private static final String DATE_FORMAT = "%s %d, %d";

    private FragmentManager mFragmentManager;
    private MainPreference mPreference;

    private Button mSubmitButton;
    private Button mBirthdayButton;
    private RadioGroup mGenderRadioGroup;
    private EditText mExpectancyEditText;

    private int mBirthdayYear;
    private int mBirthdayMonth;
    private int mBirthdayDay;

    private Gender mGender = Gender.MALE;
    private Nation mNation = Nation.TAIWAN;

    private boolean hasDialogFragment(String name) {
        final FragmentManager fragmentManager = getActivity().getFragmentManager();
        DialogFragment prev = (DialogFragment)fragmentManager.findFragmentByTag(name);

        if(prev != null) {
            return true;
        }

        return false;
    }

    private void initBirthday() {
        mBirthdayYear = mPreference.getIntValue(MainPreference.PREF_KEY_YEAR);

        if(MainPreference.PREF_VALUE_DEFAULT_INT == mBirthdayYear) {
            final Calendar calender = Calendar.getInstance();

            mBirthdayYear = calender.get(Calendar.YEAR);
            mBirthdayMonth = calender.get(Calendar.MONTH);
            mBirthdayDay = calender.get(Calendar.DAY_OF_MONTH);
        } else {
            //get data from preference
            mBirthdayMonth = mPreference.getIntValue(MainPreference.PREF_KEY_MONTH);
            mBirthdayDay = mPreference.getIntValue(MainPreference.PREF_KEY_DAY);

            mGender = Gender.values()[mPreference.getIntValue(MainPreference.PREF_KEY_GENDER)];
            mNation = Nation.values()[mPreference.getIntValue(MainPreference.PREF_KEY_NATION)];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        mExpectancyEditText = (EditText) view.findViewById(R.id.expectancy);
        setDefaultLifeExpectancy();

        mSubmitButton = (Button) view.findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(this);

        mBirthdayButton = (Button) view.findViewById(R.id.birthday);
        String monthString = new DateFormatSymbols().getMonths()[mBirthdayMonth];
        mBirthdayButton.setText(String.format(DATE_FORMAT, monthString, mBirthdayDay, mBirthdayYear));
        mBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasDialogFragment(DatePickerDialog.class.getSimpleName())) {
                    return;
                }

                //https://github.com/wdullaer/MaterialDateTimePicker
                DatePickerDialog datePickerFragment = DatePickerDialog.newInstance(
                        InfoFragment.this, mBirthdayYear, mBirthdayMonth, mBirthdayDay);
                datePickerFragment.show(getActivity().getFragmentManager(), DatePickerDialog.class.getSimpleName());
            }
        });

        mGenderRadioGroup = (RadioGroup) view.findViewById(R.id.gender);
        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int position) {
                switch (position) {
                    case R.id.male:
                        mGender = Gender.MALE;
                        break;
                    case R.id.female:
                        mGender = Gender.FEMALE;
                        break;
                    default:
                        break;
                }

                setDefaultLifeExpectancy();
            }
        });

        switch(mGender) {
            case MALE:
                ((RadioButton) view.findViewById(R.id.male)).setChecked(true);
                break;
            case FEMALE:
                ((RadioButton) view.findViewById(R.id.female)).setChecked(true);
                break;
            default:
                break;
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreference = MainPreference.getInstance();
        mFragmentManager = getFragmentManager();

        initBirthday();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPreference.setIntValue(MainPreference.PREF_KEY_YEAR, mBirthdayYear);
        mPreference.setIntValue(MainPreference.PREF_KEY_MONTH, mBirthdayMonth);
        mPreference.setIntValue(MainPreference.PREF_KEY_DAY, mBirthdayDay);
        mPreference.setIntValue(MainPreference.PREF_KEY_GENDER, mGender.ordinal());
        mPreference.setIntValue(MainPreference.PREF_KEY_NATION,  mNation.ordinal());
    }

    private int getExpectancy() {
        String expectancy = mExpectancyEditText.getText().toString();

        if(0 == expectancy.length()) {
            return Person.DEFAULT_LIFE_EXPECTANCY;
        } else {
            return Integer.valueOf(expectancy);
        }
    }

    @Override
    public void onClick(View v) {
        int expectancy = getExpectancy();

        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setParameter(mBirthdayYear, mBirthdayMonth, mBirthdayDay, expectancy, mGender, mNation);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, resultFragment, ResultFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        mBirthdayYear = year;
        mBirthdayMonth = month;
        mBirthdayDay = day;

        String monthString = new DateFormatSymbols().getMonths()[month];
        mBirthdayButton.setText(String.format(DATE_FORMAT, monthString, day, year));
    }

    private void setDefaultLifeExpectancy() {
        mExpectancyEditText.setHint(String.format(getString(R.string.age_hint), CalcLife.getDefaultLifeExpectancy(mGender, mNation)));
    }
}
