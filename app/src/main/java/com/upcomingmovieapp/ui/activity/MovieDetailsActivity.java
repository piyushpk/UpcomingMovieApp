package com.upcomingmovieapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upcomingmovieapp.R;
import com.upcomingmovieapp.interfaces.ApiServiceCaller;
import com.upcomingmovieapp.model.UpcomingMoviesModel;
import com.upcomingmovieapp.ui.adapter.ImageSlidingAdapter;
import com.upcomingmovieapp.utility.App;
import com.upcomingmovieapp.utility.CommonUtils;
import com.upcomingmovieapp.webservice.ApiConstants;
import com.upcomingmovieapp.webservice.JsonResponse;
import com.upcomingmovieapp.webservice.WebRequests;

import java.util.ArrayList;

public class MovieDetailsActivity extends ParentActivity implements ApiServiceCaller {

    private Context mContext;
    private String movieId;
    private TextView txtMovieTitleToolbar, txtMovieTitle, txtMovieOverview, txtMoviePopularity,
            txtVoteCount, txtVoteAvg;
    private RatingBar ratingBar;

    private ViewPager viewPagerImage;
    private LinearLayout linearDotPanel;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if(intent != null){
            movieId = intent.getStringExtra(getString(R.string.movie_id));
        }else{
            Toast.makeText(mContext, getString(R.string.error_data_not_present_at_this_moment), Toast.LENGTH_SHORT).show();
            finish();
        }

        txtMovieTitleToolbar = findViewById(R.id.txt_title);
        txtMovieTitle = findViewById(R.id.txt_movie_title);
        txtMovieOverview = findViewById(R.id.txt_movie_overview);
        txtMoviePopularity = findViewById(R.id.txt_movie_popularity);
        txtVoteCount = findViewById(R.id.txt_vote_count);
        txtVoteAvg = findViewById(R.id.txt_vote_avg);
        ratingBar = findViewById(R.id.rating);

        viewPagerImage = findViewById(R.id.viewPager);
        linearDotPanel = findViewById(R.id.linear_slider_dots);

        getImageDetails();
        getMovieDetails();
    }

    private void getMovieDetails(){
        showLoadingDialog();
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {

            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET,
                    ApiConstants.BASE_URL + movieId + ApiConstants.MOVIE_DETAIL_URL,
                    ApiConstants.MOVIE_DETAIL_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.MOVIE_DETAIL_URL);

        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    private void getImageDetails(){
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {

            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET,
                    ApiConstants.BASE_URL + movieId + ApiConstants.MOVIE_DETAIL_IMAGES_URL,
                    ApiConstants.MOVIE_DETAIL_IMAGES_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.MOVIE_DETAIL_IMAGES_URL);

        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    private void setImageViewPager(ArrayList<UpcomingMoviesModel> upcomingMoviesModels){

        ImageSlidingAdapter viewPagerAdapter = new ImageSlidingAdapter(mContext, upcomingMoviesModels);

        viewPagerImage.setAdapter(viewPagerAdapter);

        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for(int i = 0; i < dotsCount; i++){

            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            linearDotPanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                if (jsonResponse != null) {

                    txtMovieTitleToolbar.setText(jsonResponse.original_title);
                    txtMovieTitle.setText(jsonResponse.original_title);
                    txtMovieOverview.setText(jsonResponse.overview);
                    txtMoviePopularity.setText(jsonResponse.popularity);
                    txtVoteCount.setText(jsonResponse.vote_count);
                    txtVoteAvg.setText(jsonResponse.vote_average);
                    ratingBar.setRating(Float.parseFloat(jsonResponse.vote_average));
                }
                break;
            }
            case ApiConstants.MOVIE_DETAIL_IMAGES_URL: {
                if (jsonResponse != null) {
                    setImageViewPager(jsonResponse.backdrops);
                }
                break;
            }
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.MOVIE_DETAIL_IMAGES_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case ApiConstants.MOVIE_DETAIL_IMAGES_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
