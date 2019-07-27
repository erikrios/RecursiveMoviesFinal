package com.erikriosetiawan.recursivemoviesfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikriosetiawan.recursivemoviesfinal.FavoriteDetailsActivity;
import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TvShow> tvShows;

    public FavoriteTvShowAdapter(Context context, ArrayList<TvShow> tvShows) {
        this.context = context;
        this.tvShows = tvShows;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<TvShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(ArrayList<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public FavoriteTvShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_favorite, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteTvShowAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvTvShowTitle.setText(getTvShows().get(i).getName());
        viewHolder.btnReleaseDate.setText(getTvShows().get(i).getFirstAirDate());
        viewHolder.btnVoteCount.setText(Integer.toString(getTvShows().get(i).getVoteCount()));
        viewHolder.btnVoteAverage.setText(Double.toString(getTvShows().get(i).getVoteAverage()));

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + getTvShows().get(i).getPosterPath())
                .into(viewHolder.imgViewPoster);

        viewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent dataIntent = new Intent(context, FavoriteDetailsActivity.class);
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_ID_KEY, getTvShows().get(position).getId());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_POSTER_KEY, getTvShows().get(position).getPosterPath());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_TITLE_KEY, getTvShows().get(position).getName());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_RELEASE_DATE_KEY, getTvShows().get(position).getFirstAirDate());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_VOTE_COUNT_KEY, getTvShows().get(position).getVoteCount());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_VOTE_AVERAGE_KEY, getTvShows().get(position).getVoteAverage());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_TV_SHOW_OVERVIEW_KEY, getTvShows().get(position).getOverview());
                dataIntent.putExtra(FavoriteDetailsActivity.FAVORITE_KEY, FavoriteDetailsActivity.FAVORITE_TV_SHOW_KEY);
                context.startActivity(dataIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTvShowTitle;
        ImageView imgViewPoster;
        Button btnReleaseDate, btnVoteCount, btnVoteAverage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTvShowTitle = itemView.findViewById(R.id.tv_list_title_favorite);
            imgViewPoster = itemView.findViewById(R.id.img_list_poster_favorite);
            btnReleaseDate = itemView.findViewById(R.id.btn_list_release_date_favorite);
            btnVoteCount = itemView.findViewById(R.id.btn_list_vote_count_favorite);
            btnVoteAverage = itemView.findViewById(R.id.btn_list_vote_average_favorite);
        }
    }
}
