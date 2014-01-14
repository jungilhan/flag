package com.bulgogi.flag.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bulgogi.flag.R;
import com.bulgogi.flag.config.Constants;
import com.bulgogi.flag.fragment.PlaceholderFragment;

public class MainActivity extends TrackerActionBarActivity {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    private void init() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.accent1)));

        if (Constants.Config.NEXT_FEATURE) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

            drawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    drawerLayout,         /* DrawerLayout object */
                    R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                    R.string.drawer_open,  /* "open drawer" description for accessibility */
                    R.string.drawer_close  /* "close drawer" description for accessibility */
            ) {
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Defer code dependent on restoration of previous instance state.
            drawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    drawerToggle.syncState();
                }
            });

            drawerLayout.setDrawerListener(drawerToggle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (Constants.Config.NEXT_FEATURE) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (Constants.Config.NEXT_FEATURE) {
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            switch (item.getItemId()) {
                case R.id.action_settings:
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (Constants.Config.NEXT_FEATURE) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (Constants.Config.NEXT_FEATURE) {
            // Pass any configuration change to the drawer toggls
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }
}
