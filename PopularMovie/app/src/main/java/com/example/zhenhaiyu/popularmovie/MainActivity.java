package com.example.zhenhaiyu.popularmovie;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private DrawerLayout mDrawerLayout;
    private MainActivityFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
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
        }

        if (!mMainFragment.isInLayout()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_fragment, mMainFragment, TAG_MAIN_FRAGMENT); // Always add a tag to a fragment being inserted into container
            transaction.commit();
        }



    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_main);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()){
                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.sort_polularity_item:
                                Toast.makeText(getApplicationContext(), "sort_polularity_item", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.sort_rating_item:
                                Toast.makeText(getApplicationContext(),"sort_rating_item",Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                                return true;

                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy activity");
        super.onDestroy();

    }
}
