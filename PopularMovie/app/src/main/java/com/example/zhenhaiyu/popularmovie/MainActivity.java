package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


/* good ref to set up nav layout & action bar:
 * https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
 *
 * good ref to design library:
 * http://inthecheesefactory.com/blog/android-design-support-library-codelab/en
 */

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private static final String MyPREFERENCES = "MyPrefs" ;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainActivityFragment mMainFragment;
    SharedPreferences movieDisplayPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar
        initToolbar();

        //Initializing NavigationView
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
            setupDrawerContent(navigationView);
        }

        // Restore preferences or Set default value
        movieDisplayPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String NavPref = movieDisplayPreferences.getString(getResources().getString(R.string.nav_pref),
                        getResources().getString(R.string.sort_polularity));
        switch (NavPref){
            case "popularity.desc":
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case "vote_average.desc":
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                break;
        }

        mDrawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState != null) {
        // saved instance state, fragment may exist
        // look up the instance that already exists by tag
            Log.d(LOG_TAG, "savedInstanceState != null");
            mMainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        } else if (mMainFragment == null) {
            // only create fragment if they haven't been instantiated already
            Log.d(LOG_TAG, " create new fragment instance");
            mMainFragment = new MainActivityFragment();
        }

        if (!mMainFragment.isInLayout()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, mMainFragment, TAG_MAIN_FRAGMENT); // Always add a tag to a fragment being inserted into container
            transaction.commit();
        }

    }

    private void initToolbar() {
        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
}
