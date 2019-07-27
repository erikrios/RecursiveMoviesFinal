package com.erikriosetiawan.recursivemoviesfinal;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteMoviesDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteTvShowsDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteDetailsActivity extends AppCompatActivity {

    private ImageView imgPosterFavorite;
    private TextView tvTitleFavorite;
    private Button btnReleaseDateFavorite;
    private Button btnVoteCountFavorite;
    private Button btnVoteAverageFavorite;
    private TextView tvOverviewFavorite;
    private MaterialFavoriteButton btnFavorite;

    public static final String FAVORITE_KEY = "99jk4kjsaf";
    public static final String FAVORITE_MOVIE_KEY = "hgdt5y8Zpv";
    public static final String FAVORITE_MOVIE_ID_KEY = "hGk8h5mn50";
    public static final String FAVORITE_MOVIE_POSTER_KEY = "uinkdI8ojLj";
    public static final String FAVORITE_MOVIE_TITLE_KEY = "pinldI8ojLj";
    public static final String FAVORITE_MOVIE_RELEASE_DATE_KEY = "minkdI0ojPj";
    public static final String FAVORITE_MOVIE_VOTE_COUNT_KEY = "uy5bOp05D3";
    public static final String FAVORITE_MOVIE_VOTE_AVERAGE_KEY = "0PmmnB2Qld";
    public static final String FAVORITE_MOVIE_OVERVIEW_KEY = "qinbdI8AjLj";
    public static final String FAVORITE_TV_SHOW_KEY = "oejh78FDtr";
    public static final String FAVORITE_TV_SHOW_ID_KEY = "nB954l5mnA";
    public static final String FAVORITE_TV_SHOW_POSTER_KEY = "dfgDg543Dv";
    public static final String FAVORITE_TV_SHOW_TITLE_KEY = "Fn4gDg54kD0";
    public static final String FAVORITE_TV_SHOW_RELEASE_DATE_KEY = "LfgDg543Dz";
    public static final String FAVORITE_TV_SHOW_VOTE_COUNT_KEY = "9ytRshgUYt";
    public static final String FAVORITE_TV_SHOW_VOTE_AVERAGE_KEY = "tE4AnbmNba";
    public static final String FAVORITE_TV_SHOW_OVERVIEW_KEY = "12gbg54KDv";

    FavoriteMoviesDatabaseHelper favoriteMoviesDatabaseHelper;
    FavoriteTvShowsDatabaseHelper favoriteTvShowsDatabaseHelper;

    private static Movie favoriteMovie = new Movie();
    private static TvShow favoriteTvShow = new TvShow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details);

        imgPosterFavorite = findViewById(R.id.img_favorite_detail_poster);
        tvTitleFavorite = findViewById(R.id.tv_favorite_detail_title);
        btnReleaseDateFavorite = findViewById(R.id.btn_favorite_detail_release_date);
        btnVoteCountFavorite = findViewById(R.id.btn_detail_vote_count);
        btnVoteAverageFavorite = findViewById(R.id.btn_detail_vote_average);
        tvOverviewFavorite = findViewById(R.id.tv_favorite_detail_overview);
        btnFavorite = findViewById(R.id.btn_favorite);

        favoriteMoviesDatabaseHelper = new FavoriteMoviesDatabaseHelper(FavoriteDetailsActivity.this);
        favoriteTvShowsDatabaseHelper = new FavoriteTvShowsDatabaseHelper(FavoriteDetailsActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String favoriteKey = getIntent().getStringExtra(FAVORITE_KEY);
        final String posterPath, title, releaseDate, voteCount, voteAverage, overview;

        switch (favoriteKey) {
            case FAVORITE_MOVIE_KEY:
                final int movieId = getIntent().getIntExtra(FAVORITE_MOVIE_ID_KEY, 0);
                posterPath = getIntent().getStringExtra(FAVORITE_MOVIE_POSTER_KEY);
                title = getIntent().getStringExtra(FAVORITE_MOVIE_TITLE_KEY);
                releaseDate = getIntent().getStringExtra(FAVORITE_MOVIE_RELEASE_DATE_KEY);
                voteCount = Integer.toString(getIntent().getIntExtra(FAVORITE_MOVIE_VOTE_COUNT_KEY, 0));
                voteAverage = Double.toString(getIntent().getDoubleExtra(FAVORITE_MOVIE_VOTE_AVERAGE_KEY, 0));
                overview = getIntent().getStringExtra(FAVORITE_MOVIE_OVERVIEW_KEY);

                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w185" + posterPath)
                        .into(imgPosterFavorite);
                tvTitleFavorite.setText(title);
                btnReleaseDateFavorite.setText(releaseDate);
                btnVoteCountFavorite.setText(voteCount);
                btnVoteAverageFavorite.setText(voteAverage);
                tvOverviewFavorite.setText(overview);
                setActionBar(title);

                if (isFavoriteMovieExists(movieId)) {
                    btnFavorite.setFavorite(true);
                } else {
                    btnFavorite.setFavorite(false);
                }

                btnFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        String title = tvTitleFavorite.getText().toString();
                        if (!favorite) {
                            favoriteMoviesDatabaseHelper.deleteFavorite(movieId);
                            btnFavorite.setFavorite(false);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show();
                        } else {
                            btnFavorite.setFavorite(true);
                            saveFavoriteMovies(title, releaseDate, Integer.parseInt(voteCount), Double.parseDouble(voteAverage), posterPath, overview, movieId);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.add_favorite), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

                break;

            case FAVORITE_TV_SHOW_KEY:
                final int tvShowId = getIntent().getIntExtra(FAVORITE_TV_SHOW_ID_KEY, 0);
                posterPath = getIntent().getStringExtra(FAVORITE_TV_SHOW_POSTER_KEY);
                title = getIntent().getStringExtra(FAVORITE_TV_SHOW_TITLE_KEY);
                releaseDate = getIntent().getStringExtra(FAVORITE_TV_SHOW_RELEASE_DATE_KEY);
                voteCount = Integer.toString(getIntent().getIntExtra(FAVORITE_TV_SHOW_VOTE_COUNT_KEY, 0));
                voteAverage = Double.toString(getIntent().getDoubleExtra(FAVORITE_TV_SHOW_VOTE_AVERAGE_KEY, 0));
                overview = getIntent().getStringExtra(FAVORITE_TV_SHOW_OVERVIEW_KEY);

                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w185" + posterPath)
                        .into(imgPosterFavorite);
                tvTitleFavorite.setText(title);
                btnReleaseDateFavorite.setText(releaseDate);
                btnVoteCountFavorite.setText(voteCount);
                btnVoteAverageFavorite.setText(voteAverage);
                tvOverviewFavorite.setText(overview);
                setActionBar(title);

                if (isFavoriteTvShowExists(tvShowId)) {
                    btnFavorite.setFavorite(true);
                } else {
                    btnFavorite.setFavorite(false);
                }

                btnFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        String title = tvTitleFavorite.getText().toString();
                        if (!favorite) {
                            favoriteTvShowsDatabaseHelper.deleteFavorite(tvShowId);
                            btnFavorite.setFavorite(false);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show();
                        } else {
                            btnFavorite.setFavorite(true);
                            saveFavoriteTvShows(title, releaseDate, Integer.parseInt(voteCount), Double.parseDouble(voteAverage), posterPath, overview, tvShowId);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.add_favorite), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    private boolean isFavoriteMovieExists(Integer id) {
        List<Movie> movies = favoriteMoviesDatabaseHelper.getAllFavorite();
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFavoriteTvShowExists(Integer id) {
        List<TvShow> tvShows = favoriteTvShowsDatabaseHelper.getAllFavorite();
        for (TvShow tvShow : tvShows) {
            if (tvShow.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void saveFavoriteMovies(String title, String releaseDate, Integer voteCount, Double voteAverage, String posterPath, String overview, int id) {
        favoriteMovie.setId(id);
        favoriteMovie.setTitle(title);
        favoriteMovie.setReleaseDate(releaseDate);
        favoriteMovie.setVoteCount(voteCount);
        favoriteMovie.setVoteAverage(voteAverage);
        favoriteMovie.setPosterPath(posterPath);
        favoriteMovie.setOverview(overview);

        favoriteMoviesDatabaseHelper.addFavorite(favoriteMovie);
    }

    private void saveFavoriteTvShows(String title, String releaseDate, Integer voteCount, Double voteAverage, String posterPath, String overview, int id) {
        favoriteTvShow.setId(id);
        favoriteTvShow.setName(title);
        favoriteTvShow.setFirstAirDate(releaseDate);
        favoriteTvShow.setVoteCount(voteCount);
        favoriteTvShow.setVoteAverage(voteAverage);
        favoriteTvShow.setPosterPath(posterPath);
        favoriteTvShow.setOverview(overview);

        favoriteTvShowsDatabaseHelper.addFavorite(favoriteTvShow);
    }

    private void setActionBar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
