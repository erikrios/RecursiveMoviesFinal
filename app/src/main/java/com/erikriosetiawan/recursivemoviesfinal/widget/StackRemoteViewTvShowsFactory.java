package com.erikriosetiawan.recursivemoviesfinal.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteDatabaseContract;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteTvShowsDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;

import java.util.ArrayList;

public class StackRemoteViewTvShowsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static FavoriteTvShowsDatabaseHelper favoriteTvShowsDatabaseHelper;
    private static SQLiteDatabase database;
    private final Context mContext;
    private ArrayList<TvShow> favoriteTvShow = new ArrayList<>();
    private Cursor cursor;

    StackRemoteViewTvShowsFactory(Context context) {
        mContext = context;
        favoriteTvShowsDatabaseHelper = new FavoriteTvShowsDatabaseHelper(context);
        database = favoriteTvShowsDatabaseHelper.getWritableDatabase();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        cursor = database.rawQuery("SELECT * FROM " + FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        TvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID)));
                tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_TITLE)));
                tvShow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_POSTER_PATH)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_OVERVIEW)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_RELEASE_DATE)));
                tvShow.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_COUNT)));
                tvShow.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_AVERAGE)));

                favoriteTvShow.add(tvShow);
                cursor.moveToNext();
            } while (!(cursor.isAfterLast()));
        }
        cursor.close();

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_movie_item);

        if (favoriteTvShow.size() > 0) {
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185" + favoriteTvShow.get(position).getPosterPath())
                        .submit(512, 512)
                        .get();

                rv.setImageViewBitmap(R.id.img_item, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteTvShowWidget.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_item, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}