package com.erikriosetiawan.recursivemoviesfinal;

import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.erikriosetiawan.recursivemoviesfinal.alarm.AlarmReceiver;
import com.erikriosetiawan.recursivemoviesfinal.fragments.FavoritesFragment;
import com.erikriosetiawan.recursivemoviesfinal.fragments.MoviesFragment;
import com.erikriosetiawan.recursivemoviesfinal.fragments.TvShowsFragment;
import com.erikriosetiawan.recursivemoviesfinal.widget.UpdateWidgetMovieService;
import com.erikriosetiawan.recursivemoviesfinal.widget.UpdateWidgetTvShowService;

public class MainActivity extends AppCompatActivity {

    int movieJobId = 100;
    int tvShowJobId = 101;
    int SCHEDULE_OF_PERIOD = 1000;

    private String navigationSelect;

    static final int SETTING_REQUEST_CODE = 1;

    private AlarmReceiver alarmReceiver;
    private boolean daily, release;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    fragment = new MoviesFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    setActionBarTitle(getResources().getString(R.string.nav_movies));
                    navigationSelect = "Movies";
                    return true;

                case R.id.navigation_tv_shows:
                    fragment = new TvShowsFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    setActionBarTitle(getResources().getString(R.string.nav_tv_shows));
                    navigationSelect = "TV Shows";
                    return true;

                case R.id.navigation_favorite:
                    fragment = new FavoritesFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    setActionBarTitle(getResources().getString(R.string.nav_favorite));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movies);
        }

        startMovieJob();
        startTvShowJob();

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_view_query_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    searchIntent.putExtra("QUERY", query);
                    searchIntent.putExtra("NAVIGATION_SELECT", navigationSelect);
                    startActivity(searchIntent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_setting) {
            Intent changeSettingIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeSettingIntent);
        } else if (item.getItemId() == R.id.action_setting) {
            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(settingIntent, SETTING_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUEST_CODE) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.erikriosetiawan.recursivemoviesfinal", MODE_PRIVATE);
            release = sharedPreferences.getBoolean("Release", false);
            daily = sharedPreferences.getBoolean("Daily", false);

            if (release) {
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_RELEASE, "08:00", "Release Alarm", AlarmReceiver.ID_RELEASE);
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_RELEASE);
            }

            if (daily) {
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_DAILY, "07:00", getString(R.string.daily_reminder_message), AlarmReceiver.ID_DAILY);
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_DAILY);
            }
        }
    }

    private void startMovieJob() {
        ComponentName mServiceComponentMovie = new ComponentName(this, UpdateWidgetMovieService.class);
        JobInfo.Builder builder = new JobInfo.Builder(movieJobId, mServiceComponentMovie);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Log.d(MainActivity.class.getSimpleName(), "Movie job started");
    }

    private void startTvShowJob() {
        ComponentName mServiceComponentTvShow = new ComponentName(this, UpdateWidgetTvShowService.class);
        JobInfo.Builder builder = new JobInfo.Builder(tvShowJobId, mServiceComponentTvShow);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Log.d(MainActivity.class.getSimpleName(), "TV Show job started");
    }
}