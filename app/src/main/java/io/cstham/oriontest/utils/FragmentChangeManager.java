package io.cstham.oriontest.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    private ArrayList<Fragment> mFragments;

    private int mCurrentTab;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }


    private void initFragments() {
        for (Fragment fragment : mFragments) {
            mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commit();
        }

        setFragments(0);
    }

    public void setFragments(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragments.get(i);
            if (i == index) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentTab = index;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }
}
