package com.upcomingmovieapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upcomingmovieapp.R;
import com.upcomingmovieapp.model.UpcomingMoviesModel;
import com.upcomingmovieapp.ui.activity.MovieDetailsActivity;
import com.upcomingmovieapp.webservice.ApiConstants;

import java.util.ArrayList;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieHolder> {

    private Context mContext;
    private ArrayList<UpcomingMoviesModel> mUpcomingMoviesModels;

    public UpcomingMovieAdapter(Context context, ArrayList<UpcomingMoviesModel> upcomingMoviesModels) {
        mContext = context;
        mUpcomingMoviesModels = upcomingMoviesModels;
    }

    @Override
    public UpcomingMovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.cell_upcoming_movie, viewGroup, false);
        UpcomingMovieHolder viewHolder = new UpcomingMovieHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieHolder holder, final int position) {

        holder.txtMovieTitle.setText(mUpcomingMoviesModels.get(position).title);
        holder.txtReleaseDate.setText(mUpcomingMoviesModels.get(position).release_date);

        if (mUpcomingMoviesModels.get(position).adult.equalsIgnoreCase("false"))
            holder.txtIsAdult.setText(mContext.getString(R.string.not_adult_movie));
        else
            holder.txtIsAdult.setText(mContext.getString(R.string.adult_movie));

        try {
            if (mUpcomingMoviesModels.get(position).poster_path != null || mUpcomingMoviesModels.get(position).poster_path.isEmpty()) {
                Picasso.with(mContext)
                        .load(ApiConstants.IMAGE_URL + mUpcomingMoviesModels.get(position).poster_path)
                        .error(R.drawable.default_image)
                        .into(holder.imgThumbnail);
            }
        } catch (Exception e) {
        }

        holder.cardUpcomingMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.movie_id), mUpcomingMoviesModels.get(position).id);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mUpcomingMoviesModels != null && mUpcomingMoviesModels.size() > 0)
            return mUpcomingMoviesModels.size();
        else
            return 0;
    }

    public class UpcomingMovieHolder extends RecyclerView.ViewHolder {

        private CardView cardUpcomingMovie;
        private ImageView imgThumbnail;
        private TextView txtMovieTitle, txtReleaseDate, txtIsAdult;

        public UpcomingMovieHolder(@NonNull View itemView) {
            super(itemView);

            cardUpcomingMovie = itemView.findViewById(R.id.card_upcoming_movie);

            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);

            txtMovieTitle = itemView.findViewById(R.id.txt_movie_title);
            txtReleaseDate = itemView.findViewById(R.id.txt_release_date);
            txtIsAdult = itemView.findViewById(R.id.txt_is_adult);
        }
    }
}
