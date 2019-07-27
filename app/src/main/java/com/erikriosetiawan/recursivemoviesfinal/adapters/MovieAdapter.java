package com.erikriosetiawan.recursivemoviesfinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikriosetiawan.recursivemoviesfinal.DetailsActivity;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;
import com.erikriosetiawan.recursivemoviesfinal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_movies, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tvMovieTitle.setText(getMovies().get(i).getTitle());
        viewHolder.tvVoteAverage.setText(String.valueOf(getMovies().get(i).getVoteAverage()));
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + getMovies().get(i).getPosterPath())
                .into(viewHolder.imgMoviePoster);

        viewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Movie movie = getMovies().get(position);
                Intent movieDataIntent = new Intent(context, DetailsActivity.class);
                movieDataIntent.putExtra(DetailsActivity.MOVIE_KEY, movie);
                movieDataIntent.putExtra(DetailsActivity.KEY, DetailsActivity.MOVIE_KEY);
                context.startActivity(movieDataIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieTitle, tvVoteAverage;
        ImageView imgMoviePoster;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tv_list_title_movie);
            tvVoteAverage = itemView.findViewById(R.id.tv_list_vote_average_movie);
            imgMoviePoster = itemView.findViewById(R.id.img_list_poster_movie);
        }
    }
}
