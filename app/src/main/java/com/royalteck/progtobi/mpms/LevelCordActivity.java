package com.royalteck.progtobi.mpms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import Adapters.NewsAdapter;
import DrawerFragments.LcDrawerFrament;
import Model.News;
import Tab.SlidingTabLayout;
import Tasks.FetchNewsBdgTask;

public class LevelCordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ViewPager viewPager;
    private SlidingTabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_cord);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LcDrawerFrament drawerFragment = (LcDrawerFrament)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerlayout), toolbar);

        viewPager = (ViewPager) findViewById(R.id.lcpager);
        viewPager.setAdapter(new mypageradapter(getSupportFragmentManager()));
        tab = (SlidingTabLayout) findViewById(R.id.lctab);
        tab.setDistributeEvenly(true);
        tab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
        tab.setViewPager(viewPager);

        /*recyclerView = (RecyclerView) findViewById(R.id.lv_news_recycler);

        FetchNewsBdgTask fetchStudentsBackgroundTask = new FetchNewsBdgTask(LevelCordActivity.this);
        fetchStudentsBackgroundTask.execute();*/

    }

   /* public static List<News> getData(){
        List<News> data = new ArrayList<>();
        //int[] icons = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
        String[] newsheading = {"MATRICULATION", "PAYMENT OF SCHOOL FEES", "COURSE REGISTRATION", "LECTURE BEGINS"};
        String[] newdate = {"04-Apr-2016", "18-Apr-2016", "25-Apr-2016", "25-Apr-2016"};
        String[] newsdetail = {"Matriculation of new intake commence with the opening ceremony ",
                "School Fees payment commences with the following statistic of payment for various faculties\n{Science - #26,500; \nEducation - #25,000; \nSocial and Management Sci - #30,000; \nlaw - #35,000}.\nStu" +
                        "dents have 2week to pay up",
                "Students are to begin their course registration for the 2016/2017 Academic Session.", "Lecture begins for all students"};
        for (int i = 0; i<100 && i<100 && i<100; i++){
            News current = new News();
            current.header = newsheading[i%newsheading.length];
            current.date = newdate[i%newdate.length];
            current.caption = newsdetail[i%newsdetail.length];
            data.add(current);
        }


        return data;
    }*/

    class mypageradapter extends FragmentPagerAdapter {

        public mypageradapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment frag = new NewsFrag();
                    return frag;

                case 1:
                    Fragment fragment2 = new EventsFrag();
                    return fragment2;

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return "News";
                case 1:
                    return "Events";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2 ;
        }
    }
}
