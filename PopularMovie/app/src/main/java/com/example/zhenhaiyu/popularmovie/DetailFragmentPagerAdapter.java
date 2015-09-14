package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.example.zhenhaiyu.popularmovie.model.Review;

import java.util.List;

/**
 * Created by zhenhaiyu on 2015-09-13.
 */
public class DetailFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    final private Context mContext;
    private Movie mMovie;

    private String tabTitles[] = new String[] { "Info", "Reviews" };

    public DetailFragmentPagerAdapter(FragmentManager fragmentManager, Context context, Movie movie) {
        super(fragmentManager);
        mContext = context;
        mMovie = movie;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("PagerAdapter", "getItem: " + position);
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DetailFragment.newInstance(mMovie);
            case 1: // Fragment # 1 - This will show SecondFragment
                return DetailReviewFragment.newInstance(mMovie);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
