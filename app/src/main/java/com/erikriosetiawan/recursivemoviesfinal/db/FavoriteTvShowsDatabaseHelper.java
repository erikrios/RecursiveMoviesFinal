package com.erikriosetiawan.recursivemoviesfinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTvShowsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritetvshows.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LOG_TAG = FavoriteTvShowsDatabaseHelper.class.getSimpleName();

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    public FavoriteTvShowsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        Log.i(LOG_TAG, "Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOG_TAG, "Database Closed");
        dbHandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TV_SHOWS_TABLE = "CREATE TABLE " + FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME + " (" +
                FavoriteDatabaseContract.FavoriteTvShowsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID + " INTEGER, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL, " +
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_FAVORITE_TV_SHOWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addFavorite(TvShow tvShow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID, tvShow.getId());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_TITLE, tvShow.getName());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_RELEASE_DATE, tvShow.getFirstAirDate());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_POSTER_PATH, tvShow.getPosterPath());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_OVERVIEW, tvShow.getOverview());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_COUNT, tvShow.getVoteCount());
        values.put(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_AVERAGE, tvShow.getVoteAverage());

        db.insert(FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "Add to favorite success");
        db.close();
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME, FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID + "=" + id, null);
        Log.d(LOG_TAG, "Delete favorite success");
    }

    public List<TvShow> getAllFavorite() {
        String[] columns = {
                FavoriteDatabaseContract.FavoriteTvShowsEntry._ID,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_TITLE,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_RELEASE_DATE,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_POSTER_PATH,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_OVERVIEW,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_COUNT,
                FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_AVERAGE,
        };
        String sortOrder = FavoriteDatabaseContract.FavoriteTvShowsEntry._ID + " ASC";
        List<TvShow> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteDatabaseContract.FavoriteTvShowsEntry.TABLE_NAME, columns, null, null, null, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                TvShow tvShow = new TvShow();
                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID))) && TextUtils.isDigitsOnly(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID)))) {
                    tvShow.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_ID))));
                } else {
                  tvShow.setId(0);
                }
                tvShow.setName(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_TITLE)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_RELEASE_DATE)));
                tvShow.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_POSTER_PATH)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_OVERVIEW)));
                tvShow.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_COUNT))));
                tvShow.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteDatabaseContract.FavoriteTvShowsEntry.COLUMN_VOTE_AVERAGE))));

                favoriteList.add(tvShow);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }
}
