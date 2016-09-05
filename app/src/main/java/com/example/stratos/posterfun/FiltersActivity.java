package com.example.stratos.posterfun;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.stratos.posterfun.fragments.FilterCategoryFragment;
import com.example.stratos.posterfun.fragments.FilterCityFragment;
import com.example.stratos.posterfun.fragments.FilterDateFragment;
import com.example.stratos.posterfun.fragments.FilterOtherFragment;
import com.example.stratos.posterfun.fragments.FilterSortFragment;
import com.example.stratos.posterfun.utils.PreCont;

public class FiltersActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String[] mTitles;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mTitles = getResources().getStringArray(R.array.page_titls);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_brightness_low_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_date_range_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_done_all_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_sort_white_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_location_city_white_24dp);
        int selTabIndex = getIntent().getIntExtra(PreCont.ARG_SECTION_TAB, 0);
        if(selTabIndex != 0) {
            TabLayout.Tab startTab = tabLayout.getTabAt(selTabIndex);
            startTab.select();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Сделайте выбор", Toast.LENGTH_SHORT).show();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FilterCategoryFragment();
                case 1:
                    return new FilterDateFragment();
                case 2:
                    return new FilterOtherFragment();
                case 3:
                    return new FilterSortFragment();
                case 4:
                    return new FilterCityFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
