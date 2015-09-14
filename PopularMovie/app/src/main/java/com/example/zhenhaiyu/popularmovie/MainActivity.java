package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.zhenhaiyu.popularmovie.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;


/* good ref to set up nav layout & action bar:
 * https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
 *
 * good ref to design library:
 * http://inthecheesefactory.com/blog/android-design-support-library-codelab/en
 *
 * example of Utilizing Http response cache:
 * http://practicaldroid.blogspot.ca/2013/01/utilizing-http-response-cache.html
 *
 * official doc of Supporting Tablets and Handsets:
 * http://developer.android.com/guide/practices/tablets-and-handsets.html
 *
 * tutorial on customized listener:
 * https://guides.codepath.com/android/Creating-Custom-Listeners
 */

public class MainActivity extends AppCompatActivity implements MovieClickListener{
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final Context mContext = this;
    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private static final String MyPREFERENCES = "MyPrefs" ;

    private MainActivityFragment mMainFragment;
    private SharedPreferences movieDisplayPreferences;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mMultiScreen;

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
//    @Bind(R.id.nav_view) NavigationView mNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMultiScreen = findViewById(R.id.detail_container) != null;
        Log.d(LOG_TAG, "mMultiScreen = " + mMultiScreen);

        // Initializing Toolbar
        initToolbar();

        //Initializing NavigationView
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
            setupDrawerContent(mNavigationView);
        }
        mDrawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Restore preferences or Set default value
        movieDisplayPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String NavPref = movieDisplayPreferences.getString(getResources().getString(R.string.nav_pref),
                        getResources().getString(R.string.sort_polularity));
        switch (NavPref){
            case "popularity.desc":
                mNavigationView.getMenu().getItem(0).setChecked(true);
                break;
            case "vote_average.desc":
                mNavigationView.getMenu().getItem(1).setChecked(true);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                break;
        }


        if (savedInstanceState != null) {
        // saved instance state, fragment may exist
        // look up the instance that already exists by tag
            Log.d(LOG_TAG, "savedInstanceState != null");
            mMainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        } else if (mMainFragment == null) {
            // only create fragment if they haven't been instantiated already
            Log.d(LOG_TAG, " create new fragment instance");
            mMainFragment = new MainActivityFragment();
            mMainFragment.setMovieClickListener(this);
//            // Setup the listener for MainActivityFragment
//            mMainFragment.setMovieClickListener(new MovieClickListener() {
//                @Override
//                public void movieSelected(View view, Movie movie) {
//                    Log.d(LOG_TAG, "movieSelected " + movie.title);
//                    Intent detailIntent = new Intent(mContext, DetailActivity.class);
//                    detailIntent.putExtra(getResources().getString(R.string.title_activity_detail), movie);
//                    startActivity(detailIntent);
//                }
//            });
        }

        if (!mMainFragment.isInLayout()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frag_container, mMainFragment, TAG_MAIN_FRAGMENT); // Always add a tag to a fragment being inserted into container
            transaction.commit();
        }

    }


    private void initToolbar() {
        // Set a Toolbar to replace the ActionBar.
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_main);
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu); // The ActionBarDrawerToggle renders a custom DrawerArrowDrawable
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        SharedPreferences.Editor editor = movieDisplayPreferences.edit();
                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.sort_polularity_item:
//                                Toast.makeText(getApplicationContext(), "sort_polularity_item", Toast.LENGTH_SHORT).show();
                                editor.putString(getResources().getString(R.string.nav_pref), getResources().getString(R.string.sort_polularity));
                                editor.commit();
                                return true;

                            case R.id.sort_rating_item:
//                                Toast.makeText(getApplicationContext(), "sort_rating_item", Toast.LENGTH_SHORT).show();
                                editor.putString(getResources().getString(R.string.nav_pref), getResources().getString(R.string.sort_rating));
                                editor.commit();
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                                return true;

                        }
                    }
                });

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

        mMainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        if (mMainFragment == null) {
            // only create fragment if they haven't been instantiated already
            Log.d(LOG_TAG, " create new fragment instance");
            mMainFragment = new MainActivityFragment();
            mMainFragment.setMovieClickListener(this);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frag_container, mMainFragment, TAG_MAIN_FRAGMENT); // Always add a tag to a fragment being inserted into container
            transaction.commit();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy activity");
        super.onDestroy();

    }

    @Override
    public void movieSelected(View view, Movie movie) {
        Log.d(LOG_TAG, "movieSelected " + movie.title);

        if (findViewById(R.id.detail_container) != null){




        }
        else {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(getResources().getString(R.string.title_activity_detail), movie);
            startActivity(detailIntent);
        }
    }

}
