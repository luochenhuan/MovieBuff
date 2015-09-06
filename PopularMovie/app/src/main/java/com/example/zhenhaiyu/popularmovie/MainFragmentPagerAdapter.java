package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by zhenhaiyu on 2015-08-23.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    final private Context mContext;

    private int[] imageResId = {
            R.drawable.ic_trend,
            R.drawable.ic_rate,
    };

    private String tabTitles[] = new String[] { "sorted by popularity", "sorted by rating" };

    public MainFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return MainActivityFragment.newInstance(0);
            case 1: // Fragment # 1 - This will show SecondFragment
                return MainActivityFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
//        Drawable image;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            image = mContext.getResources().getDrawable(imageResId[position], null);
//        }
//        else {
//            image = mContext.getResources().getDrawable(imageResId[position]);
//        }
//        SpannableString sb = new SpannableString("   " + tabTitles[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        return sb;
        return tabTitles[position];
    }
}