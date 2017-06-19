package com.dmitriytitov.ritgtesttask;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dmitriytitov.ritgtesttask.data.DataLoader;
import com.dmitriytitov.ritgtesttask.fragments.DataListFragment;
import com.dmitriytitov.ritgtesttask.fragments.MusicFragment;
import com.dmitriytitov.ritgtesttask.fragments.RegistrationFragment;

public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_tab_1,
            R.drawable.ic_tab_2,
            R.drawable.ic_tab_3,
            R.drawable.ic_tab_4,
            R.drawable.ic_tab_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return createDataListFragment(DataLoader.RequestType.COMBINED);
                case 1:
                    return createDataListFragment(DataLoader.RequestType.HTTP);
                case 2:
                    return createDataListFragment(DataLoader.RequestType.SQLITE);
                case 3:
                    return new MusicFragment();
                case 4:
                    return new RegistrationFragment();
            }
            return null;
        }


        private Fragment createDataListFragment(DataLoader.RequestType requestType) {
            Fragment fragment = new DataListFragment();
            Bundle args = new Bundle();
            args.putSerializable("requestType", requestType);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
