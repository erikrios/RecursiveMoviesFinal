package com.erikriosetiawan.recursivemoviesfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovies;
    private ArrayList<TvShow> listTvShows;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getListMovies() {
        return listMovies;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
    }

    public ArrayList<TvShow> getListTvShows() {
        return listTvShows;
    }

    public void setListTvShows(ArrayList<TvShow> listTvShows) {
        this.listTvShows = listTvShows;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_movies, viewGroup, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
        if (getListMovies() == null) {
            Picasso.get().load("https://image.tmdb.org/t/p/w185" + getListTvShows().get(i).getPosterPath())
                    .into(viewHolder.imgPoster);
            viewHolder.tvTitle.setText(getListTvShows().get(i).getName());
            viewHolder.tvVoteAverage.setText(Double.toString(getListTvShows().get(i).getVoteAverage()));
        } else {
            Picasso.get().load("https://image.tmdb.org/t/p/w185" + getListMovies().get(i).getPosterPath())
                    .into(viewHolder.imgPoster);
            viewHolder.tvTitle.setText(getListMovies().get(i).getTitle());
            viewHolder.tvVoteAverage.setText(Double.toString(getListMovies().get(i).getVoteAverage()));
        }
    }

    @Override
    public int getItemCount() {
        if (getListTvShows() == null) {
            return getListMovies().size();
        } else {
            return getListTvShows().size();
        }
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvTitle, tvVoteAverage;
         ViewHolder(@NonNull View itemView) {
            super(itemView);

             imgPoster = itemView.findViewById(R.id.img_list_poster_movie);
             tvTitle = itemView.findViewById(R.id.tv_list_title_movie);
             tvVoteAverage = itemView.findViewById(R.id.tv_list_vote_average_movie);
        }
    }

    public void clearList(String type) {
        int range;
        if (type.equals("Movies")) {
            range = listMovies.size();
            listMovies.clear();
        } else {
            range = listTvShows.size();
            listMovies.clear();
        }
        notifyItemRangeRemoved(0, range);
    }
}
