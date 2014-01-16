package com.bulgogi.flag.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.bulgogi.flag.R;
import com.bulgogi.flag.adapter.CategoryAdapter;
import com.bulgogi.flag.config.Constants;
import com.bulgogi.flag.fragment.FlagFragment;
import com.bulgogi.flag.model.CategoryItem;
import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends TrackerActionBarActivity {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private int category = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        if (savedInstanceState != null) {
            category = savedInstanceState.getInt(Constants.BUNDLE_LAST_CATEGORY);
        }

        selectCategory(category);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.BUNDLE_LAST_CATEGORY, category);
    }

    private void init() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.accent1)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close  ) {
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

        setupCategory();
    }

    private void setupCategory() {
        PinnedSectionListView category = (PinnedSectionListView) findViewById(R.id.category);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCategory(position);
            }
        });

        List<CategoryItem> items = new ArrayList<CategoryItem>();
        items.add(CategoryItem.valueOf(getResources().getString(R.string.drawer_section_0), CategoryItem.SECTION));
        List<String> names = Arrays.asList(getResources().getStringArray(R.array.national_flag_category_items));
        for (String name : names) {
            items.add(CategoryItem.valueOf(name, CategoryItem.ITEM));
        }

        items.add(CategoryItem.valueOf(getResources().getString(R.string.drawer_section_1), CategoryItem.SECTION));
        List<String> leagues = Arrays.asList(getResources().getStringArray(R.array.football_category_items));
        for (String league : leagues) {
            items.add(CategoryItem.valueOf(league, CategoryItem.ITEM));
        }

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.tv_category_item, items);
        category.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
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

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void selectCategory(int position) {
        category = position;

        Fragment fragment = null;
        switch (position) {
            case 0: // National flags header
                break;
            case 1:
                fragment = FlagFragment.newInstance(getResources().getStringArray(R.array.national_flag_category_items)[0], R.array.countries, R.array.country_thumbs, R.array.country_flags, false);
                break;
            case 2:
                fragment = FlagFragment.newInstance(getResources().getStringArray(R.array.national_flag_category_items)[1], R.array.worldcup2014_countries, R.array.worldcup2014_thumbs, R.array.worldcup2014_flags, false);
                break;
            case 3:
                fragment = FlagFragment.newInstance(getResources().getStringArray(R.array.national_flag_category_items)[2], R.array.olympic2014_countries, R.array.olympic2014_thumbs, R.array.olympic2014_flags, false);
                break;
            case 4: // Football emblems header
                break;
            case 5:
                fragment = FlagFragment.newInstance("EPL", R.array.epl_clubs, R.array.epl_thumbs, R.array.epl_flags, true);
                break;
            case 6:
                fragment = FlagFragment.newInstance("Serie A", R.array.seriea_clubs, R.array.seriea_thumbs, R.array.seriea_flags, true);
                break;
            case 7:
                fragment = FlagFragment.newInstance("Primera Liga", R.array.primeraliga_clubs, R.array.primeraliga_thumbs, R.array.primeraliga_flags, true);
                break;
            case 8:
                fragment = FlagFragment.newInstance("Bundesliga", R.array.bundesliga_clubs, R.array.bundesliga_thumbs, R.array.bundesliga_flags, true);
                break;
        }

        if (fragment != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }
}
