package com.erikriosetiawan.recursivemoviesfinal;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY = "15ErSNmAfb";
    public static final String MOVIE_KEY = "ruavTTNyB2";
    public static final String TV_SHOW_KEY = "KHzxKxndNC";

    private ImageView imgPoster;
    private TextView tvTitle;
    private Button btnReleaseDate;
    private Button btnVoteCount;
    private Button btnVoteAverage;
    private TextView tvOverview;
    private MaterialFavoriteButton btnFavorite;

    private static FavoriteMoviesDatabaseHelper favoriteMoviesDatabaseHelper;
    private FavoriteTvShowsDatabaseHelper favoriteTvShowsDatabaseHelper;
    private static Movie favoriteMovie = new Movie();
    private static TvShow favoriteTvShow = new TvShow();
    private final AppCompatActivity activity = DetailsActivity.this;

    private List<Movie> movies;
    private List<TvShow> tvShows;

    private String title, releaseDate, overview, posterPath;
    private int id, voteCount;
    private double voteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imgPoster = findViewById(R.id.img_favorite_detail_poster);
        tvTitle = findViewById(R.id.tv_favorite_detail_title);
        btnReleaseDate = findViewById(R.id.btn_favorite_detail_release_date);
        btnVoteCount = findViewById(R.id.btn_detail_vote_count);
        btnVoteAverage = findViewById(R.id.btn_detail_vote_average);
        tvOverview = findViewById(R.id.tv_favorite_detail_overview);
        btnFavorite = findViewById(R.id.btn_favorite);

        favoriteMoviesDatabaseHelper = new FavoriteMoviesDatabaseHelper(activity);
        favoriteTvShowsDatabaseHelper = new FavoriteTvShowsDatabaseHelper(activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String key = getIntent().getStringExtra(KEY);

        switch (key) {

            case MOVIE_KEY:
                final Movie movie = getIntent().getParcelableExtra(MOVIE_KEY);
                title = movie.getTitle();
                overview = movie.getOverview();
                releaseDate = movie.getReleaseDate();
                voteCount = movie.getVoteCount();
                voteAverage = movie.getVoteAverage();
                posterPath = movie.getPosterPath();
                id = movie.getId();
                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w185" + posterPath)
                        .into(imgPoster);
                tvTitle.setText(title);
                btnReleaseDate.setText(releaseDate);
                btnVoteCount.setText(String.valueOf(movie.getVoteCount()));
                btnVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
                tvOverview.setText(overview);
                setActionBar(tvTitle.getText().toString());

                if (isFavoriteMovieExists(movie)) {
                    btnFavorite.setFavorite(true);
                } else {
                    btnFavorite.setFavorite(false);
                }

                btnFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (!favorite) { // Exists in database
                            favoriteMoviesDatabaseHelper.deleteFavorite(id);
                            btnFavorite.setFavorite(false);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show();
                        } else {
                            saveFavoriteMovies(title, releaseDate, voteCount, voteAverage, posterPath, overview, id);
                            btnFavorite.setFavorite(true);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.add_favorite), Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
                break;

            case TV_SHOW_KEY:
                final TvShow tvShow = getIntent().getParcelableExtra(TV_SHOW_KEY);
                title = tvShow.getName();
                overview = tvShow.getOverview();
                releaseDate = tvShow.getFirstAirDate();
                voteCount = tvShow.getVoteCount();
                voteAverage = tvShow.getVoteAverage();
                posterPath = tvShow.getPosterPath();
                id = tvShow.getId();
                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w185" + tvShow.getPosterPath())
                        .into(imgPoster);
                tvTitle.setText(title);
                btnReleaseDate.setText(releaseDate);
                btnVoteCount.setText(String.valueOf(tvShow.getVoteCount()));
                btnVoteAverage.setText(String.valueOf(tvShow.getVoteAverage()));
                tvOverview.setText(overview);
                setActionBar(tvTitle.getText().toString());

                if (isFavoriteTvShowExists(tvShow)) {
                    btnFavorite.setFavorite(true);
                } else {
                    btnFavorite.setFavorite(false);
                }

                btnFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (!favorite) {
                            favoriteTvShowsDatabaseHelper.deleteFavorite(id);
                            btnFavorite.setFavorite(false);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show();
                        } else {
                            saveFavoriteTvShows(title, releaseDate, voteCount, voteAverage, posterPath, overview, id);
                            btnFavorite.setFavorite(true);
                            Snackbar.make(buttonView, title + " " + getResources().getString(R.string.add_favorite), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    private void setActionBar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
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

    private boolean isFavoriteMovieExists(Movie movieList) {
        movies = favoriteMoviesDatabaseHelper.getAllFavorite();
        for (Movie movie : movies) {
            if (movie.getId().equals(movieList.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isFavoriteTvShowExists(TvShow tvShowList) {
        tvShows = favoriteTvShowsDatabaseHelper.getAllFavorite();
        for (TvShow tvShow : tvShows) {
            if (tvShow.getId().equals(tvShowList.getId())) {
                return true;
            }
        }
        return false;
    }
}
