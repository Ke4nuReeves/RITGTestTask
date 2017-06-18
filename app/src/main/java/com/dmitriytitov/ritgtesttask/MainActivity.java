package com.dmitriytitov.ritgtesttask;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dmitriytitov.ritgtesttask.data.DataLoader;
import com.dmitriytitov.ritgtesttask.fragments.DataListFragment;
import com.dmitriytitov.ritgtesttask.fragments.MusicFragment;
import com.dmitriytitov.ritgtesttask.fragments.RegistrationFragment;

public class MainActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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

        //TODO change text to images
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1_name);
                case 1:
                    return getString(R.string.tab2_name);
                case 2:
                    return getString(R.string.tab3_name);
                case 3:
                    return getString(R.string.tab4_name);
                case 4:
                    return getString(R.string.tab5_name);
            }
            return null;
        }
    }
}
