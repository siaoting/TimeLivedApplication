package com.violet.TimeLivedApplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.violet.TimeLivedApplication.UiComponent.InfoFragment;

public class MainActivity extends AppCompatActivity {
    private static final int TOP_BACKSTACK = 1;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getFragmentManager();

        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, new InfoFragment(), InfoFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        //backstack(1) is the InfoFragment.
        if (mFragmentManager.getBackStackEntryCount() > TOP_BACKSTACK) {
            mFragmentManager.popBackStack();
        } else {
            this.finish();
        }
    }
}
