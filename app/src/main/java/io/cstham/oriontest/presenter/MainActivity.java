package io.cstham.oriontest.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.flyco.tablayout.CommonTabLayout;
//import com.flyco.tablayout.listener.CustomTabEntity;
//import com.flyco.tablayout.listener.OnTabSelectListener;

import io.cstham.oriontest.R;
import io.cstham.oriontest.utils.MyTabLayout;
import io.cstham.oriontest.utils.CustomTabEntity;
import io.cstham.oriontest.utils.OnTabSelectListener;


import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import io.cstham.oriontest.view.HistoryFragment;
import io.cstham.oriontest.view.MainFragment;
import io.cstham.oriontest.view.SearchBarFragment;


import io.cstham.oriontest.utils.tabproperties.NonSwipeableViewPager;
import io.cstham.oriontest.utils.tabproperties.TabEntity;
import io.cstham.oriontest.utils.tabproperties.ViewFindUtils;

import io.cstham.oriontest.database.DatabaseHelper;
import io.cstham.oriontest.database.HistoryDatabaseHelper;
import io.cstham.oriontest.entity.Record;


//import static io.cstham.oriontest.Fragments.MainFragment.client;


public class MainActivity extends FragmentActivity {
    //==================================================================================================
    //PERMISSIONS VARIABLES

    //private PermissionRequestErrorListener errorListener;
    //private WebsocketClient websocketClient;
    //==================================================================================================
    //tab layout variables
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    MainFragment mainFragment = new MainFragment();
    HistoryFragment historyFragment = new HistoryFragment();


    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<Integer> mIconUnselectIds = new ArrayList<>();
    private ArrayList<Integer> mIconSelectIds = new ArrayList<>();

    private HistoryDatabaseHelper db;

    //private String[] mTitles = {"HOME", "HISTORY", "CAMERA", "CONTACTS"};
/*
    private int[] mIconUnselectIds = {
            R.mipmap.i_home, R.mipmap.i_chat,
            R.mipmap.i_camera, R.mipmap.i_ppl};

    private int[] mIconSelectIds = {
            R.mipmap.i_home, R.mipmap.i_chat,
            R.mipmap.i_camera, R.mipmap.i_ppl};
*/
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    //private ViewPager mViewPager;
    private NonSwipeableViewPager mViewPager;

    public static MyTabLayout mTabLayout;

    SharedPreferences prefs;

    //PlayerInfo playerInfo = new PlayerInfo();

    //String username, depositId, amount, balance, status, signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_tabs);
        db = new HistoryDatabaseHelper(this);
        //db.deleteAllHistories();
        /*
        db = new DatabaseHelper(this);
        long id = db.insertRecord("title","author","desc","url","image",
                "content");
        Record n = db.getRecord(id);



        db = new DatabaseHelper(this);
        long id = db.insertHistory(
                "LOL",
                "",
                "",
                "",
                "",
                "");
*/
        //==========================================================================================
        //TAB LAYOUT
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String member_role = prefs.getString("member_role","");
        String member_username = prefs.getString("member_username","");

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

        //==========================================================================================
        //create sql database
        //SQLiteDatabase articlesDB = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);


    }



    private void tab_behaviour() {
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

/*
                if (position != mFragments.size()-1) {
                    mViewPager.setCurrentItem(position, false);   //smoothScroll = false, to disable page scroll animations

                }
*/
                mViewPager.setCurrentItem(position, false);   //smoothScroll = false, to disable page scroll animations
                //this line is very important to preserve screen state during tab transitions
                //this line can also prevent each fragment from being called everytime the user touches it
                //(prevent re-loading)
                mViewPager.setOffscreenPageLimit(mFragments.size()-1);

//            if (position == 1){
//                System.out.println("get all histories lolz: "+db.getAllHistories());
//            }



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

        /*
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
        */

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
//==================================================================================================



    @Override
    public void onBackPressed() {

        //finish();
        /*
        if (SClick.check("exit-intended", 1250)) {
            // Ask user if really want to quit
            Toast.makeText(this, "Double tap ESC to quit", Toast.LENGTH_LONG).show();
        } else {
            // Quit app
            finishAffinity();
        }
        */
        finishAffinity();
    }


}
