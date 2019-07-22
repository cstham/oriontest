package io.cstham.oriontest.presenter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import io.cstham.oriontest.R;
import io.cstham.oriontest.utils.MyTabLayout;
import io.cstham.oriontest.utils.CustomTabEntity;
import io.cstham.oriontest.utils.OnTabSelectListener;
import java.util.ArrayList;
import io.cstham.oriontest.view.HistoryFragment;
import io.cstham.oriontest.view.MainFragment;
import io.cstham.oriontest.utils.tabproperties.NonSwipeableViewPager;
import io.cstham.oriontest.utils.tabproperties.TabEntity;
import io.cstham.oriontest.utils.tabproperties.ViewFindUtils;

public class MainActivity extends FragmentActivity {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    MainFragment mainFragment = new MainFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<Integer> mIconUnselectIds = new ArrayList<>();
    private ArrayList<Integer> mIconSelectIds = new ArrayList<>();

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    //private ViewPager mViewPager;
    private NonSwipeableViewPager mViewPager;

    public static MyTabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_tabs);
        //==========================================================================================
        //TAB LAYOUT
        //Main Fragment
        mFragments.add(mainFragment);               //home
        mTitles.add("HOME");
        mIconUnselectIds.add(R.mipmap.i_home);
        mIconSelectIds.add(R.mipmap.i_home);

        mFragments.add(historyFragment);         //history
        mTitles.add("HISTORY");
        mIconUnselectIds.add(R.mipmap.i_chat);
        mIconSelectIds.add(R.mipmap.i_chat);

        for (int i = 0; i < mTitles.size(); i++) {
            mTabEntities.add(new TabEntity(mTitles.get(i), mIconSelectIds.get(i), mIconUnselectIds.get(i)));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.viewPager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout = ViewFindUtils.find(mDecorView, R.id.myTab);

        tab_behaviour();

    }



    private void tab_behaviour() {
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position, false);   //smoothScroll = false, to disable page scroll animations
                mViewPager.setOffscreenPageLimit(mFragments.size()-1);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
//==================================================================================================

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
