package com.erikriosetiawan.recursivemoviesfinal;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.erikriosetiawan.recursivemoviesfinal.fragments.FavoritesFragment;
import com.erikriosetiawan.recursivemoviesfinal.fragments.MoviesFragment;
import com.erikriosetiawan.recursivemoviesfinal.fragments.TvShowsFragment;
import com.erikriosetiawan.recursivemoviesfinal.widget.UpdateWidgetMovieService;

public class MainActivity extends AppCompatActivity {

    int jobId = 1001;
    int SCHEDULE_OF_PERIOD = 1000;

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
                    return true;

                case R.id.navigation_tv_shows:
                    fragment = new TvShowsFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_home, fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    setActionBarTitle(getResources().getString(R.string.nav_tv_shows));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_setting) {
            Intent changeSettingIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeSettingIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void startMovieJob() {
        ComponentName mServiceComponentMovie = new ComponentName(this, UpdateWidgetMovieService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponentMovie);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Log.d(MainActivity.class.getSimpleName(), "Job started");
    }

}
