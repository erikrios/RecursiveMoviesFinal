package com.erikriosetiawan.recursivemoviesfinal.widget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

public class UpdateWidgetMovieService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(this, FavoriteMovieWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), FavoriteMovieWidget.class));

        FavoriteMovieWidget favoriteMovieWidget = new FavoriteMovieWidget();
        favoriteMovieWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
