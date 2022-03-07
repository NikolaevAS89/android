package ru.timestop.android.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.timestop.android.model.Movie;
import ru.timestop.android.myapplication.R;

public class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.MovieViewHolder> {

    private Context context;
    private List<Movie> movies;

    public MovieAdaptor(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sheet, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdaptor.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleMove.setText(movie.getTitle());
        holder.yearMove.setText(movie.getYear());
        holder.imdbId.setText(movie.getImdbid());
        Log.d("picasso", "start ->" + movie.getPosterUrl());
        Picasso.get().load(movie.getPosterUrl()).fit().into(holder.posterImageView);
        Log.d("picasso", "end");
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleMove;
        TextView yearMove;
        TextView imdbId;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleMove = itemView.findViewById(R.id.titleMove);
            yearMove = itemView.findViewById(R.id.yearMove);
            imdbId = itemView.findViewById(R.id.imdbId);
        }
    }
}
