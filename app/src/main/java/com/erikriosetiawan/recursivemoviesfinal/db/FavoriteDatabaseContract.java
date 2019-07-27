package com.erikriosetiawan.recursivemoviesfinal.db;

import android.provider.BaseColumns;

public class FavoriteDatabaseContract {

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final String TABLE_NAME = "favoritemovies";
        public static final String COLUMN_ID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_RELEASE_DATE = "releasedate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_COUNT = "votecount";
        public static final String COLUMN_VOTE_AVERAGE = "voteaverage";
    }

    public static final class FavoriteTvShowsEntry implements BaseColumns {

        public static final String TABLE_NAME = "favoritetvshows";
        public static final String COLUMN_ID = "tvshowid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_RELEASE_DATE = "releasedate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_COUNT = "votecount";
        public static final String COLUMN_VOTE_AVERAGE = "voteaverage";
    }

}
