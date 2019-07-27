package com.erikriosetiawan.recursivemoviesfinal.adapters;

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

import com.erikriosetiawan.recursivemoviesfinal.DetailsActivity;
import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TvShow> tvShows;

    public TvShowAdapter(Context context, ArrayList<TvShow> tvShows) {
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
    public TvShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_tv_shows, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvTvShowTitle.setText(getTvShows().get(i).getName());
        viewHolder.btnTvShowVoteCount.setText(String.valueOf(getTvShows().get(i).getVoteCount()));
        viewHolder.btnTvShowVoteAverage.setText(String.valueOf(getTvShows().get(i).getVoteAverage()));
        viewHolder.btnTvShowReleaseDate.setText(getTvShows().get(i).getFirstAirDate());

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + getTvShows().get(i).getPosterPath())
                .into(viewHolder.imgTvShowPoster);

        viewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                TvShow tvShow = getTvShows().get(position);
                Intent tvShowDataIntent = new Intent(context, DetailsActivity.class);
                tvShowDataIntent.putExtra(DetailsActivity.TV_SHOW_KEY, tvShow);
                tvShowDataIntent.putExtra(DetailsActivity.KEY, DetailsActivity.TV_SHOW_KEY);
                context.startActivity(tvShowDataIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return getTvShows().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTvShowTitle;
        Button btnTvShowVoteCount, btnTvShowVoteAverage, btnTvShowReleaseDate;
        ImageView imgTvShowPoster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTvShowTitle = itemView.findViewById(R.id.tv_list_title_tv_shows);
            btnTvShowVoteCount = itemView.findViewById(R.id.btn_list_vote_count_favorite);
            btnTvShowVoteAverage = itemView.findViewById(R.id.btn_list_vote_average_favorite);
            btnTvShowReleaseDate = itemView.findViewById(R.id.btn_list_release_date_tv_shows);
            imgTvShowPoster = itemView.findViewById(R.id.img_list_poster_tv_shows);
        }
    }
}
