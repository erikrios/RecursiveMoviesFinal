package com.erikriosetiawan.recursivemoviesfinal.widget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

public class UpdateWidgetTvShowService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(this, FavoriteTvShowWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), FavoriteTvShowWidget.class));

        FavoriteTvShowWidget favoriteTvShowWidget = new FavoriteTvShowWidget();
        favoriteTvShowWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}