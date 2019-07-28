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
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteMoviesDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;

import java.util.ArrayList;

public class StackRemoteViewMoviesFactory implements RemoteViewsService.RemoteViewsFactory {

    private static FavoriteMoviesDatabaseHelper favoriteMoviesDatabaseHelper;
    private static SQLiteDatabase database;
    private final Context mContext;
    private ArrayList<Movie> favoriteMovie = new ArrayList<>();
    private Cursor cursor;

    StackRemoteViewMoviesFactory(Context context) {
        mContext = context;
        favoriteMoviesDatabaseHelper = new FavoriteMoviesDatabaseHelper(context);
        database = favoriteMoviesDatabaseHelper.getWritableDatabase();
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

        cursor = database.rawQuery("SELECT * FROM " + FavoriteDatabaseContract.FavoriteMoviesEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_VOTE_COUNT)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteDatabaseContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE)));

                favoriteMovie.add(movie);
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
        return favoriteMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (favoriteMovie.size() > 0) {
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185" + favoriteMovie.get(position).getPosterPath())
                        .submit(512, 512)
                        .get();

                rv.setImageViewBitmap(R.id.img_item, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);

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
