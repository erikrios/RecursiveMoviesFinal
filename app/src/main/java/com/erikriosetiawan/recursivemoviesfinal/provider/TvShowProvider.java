package com.erikriosetiawan.recursivemoviesfinal.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteDatabaseContract;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteTvShowsDatabaseHelper;

import java.util.HashMap;
import java.util.Objects;

import static com.erikriosetiawan.recursivemoviesfinal.db.FavoriteDatabaseContract.FavoriteTvShowsEntry.*;

public class TvShowProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.erikriosetiawan.recursivemoviesfinal.TvShowsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/" + TABLE_NAME;
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = FavoriteDatabaseContract.FavoriteTvShowsEntry._ID;
    static final String TITLE = COLUMN_TITLE;

    private HashMap<String, String> TV_SHOWS_PROJECTION_MAP = new HashMap<>();

    static final int TV_SHOWS = 1;
    static final int TV_SHOW_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME, TV_SHOWS);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME + "/#", TV_SHOW_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        FavoriteTvShowsDatabaseHelper favoriteTvShowsDatabaseHelper = new FavoriteTvShowsDatabaseHelper(context);
        database = favoriteTvShowsDatabaseHelper.getWritableDatabase();
        return database != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case TV_SHOWS:
                queryBuilder.setProjectionMap(TV_SHOWS_PROJECTION_MAP);
                break;
            case TV_SHOW_ID:
                queryBuilder.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || Objects.equals(sortOrder, "")) {
            sortOrder = TITLE;
        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TV_SHOWS:
                return "vnd.android.cursor.dir/vnd.example" + "." + TABLE_NAME;
            case TV_SHOW_ID:
                return "vnd.android.cursor.item/vnd.example" + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = database.insert(TABLE_NAME, "", values);

        if (rowId > 0) {
            Uri myUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(myUri, null);
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case TV_SHOWS:
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case TV_SHOW_ID:
                String id = uri.getPathSegments().get(1);
                count = database.delete(TABLE_NAME, _ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknowns URI " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case TV_SHOWS:
                count = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case TV_SHOW_ID:
                count = database.update(TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return count;
    }
}