package com.upcomingmovieapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upcomingmovieapp.R;
import com.upcomingmovieapp.interfaces.ApiServiceCaller;
import com.upcomingmovieapp.ui.adapter.UpcomingMovieAdapter;
import com.upcomingmovieapp.utility.App;
import com.upcomingmovieapp.utility.CommonUtils;
import com.upcomingmovieapp.webservice.ApiConstants;
import com.upcomingmovieapp.webservice.JsonResponse;
import com.upcomingmovieapp.webservice.WebRequests;

public class LandingActivity extends ParentActivity implements ApiServiceCaller {

    private Context mContext;
    private RecyclerView recyclerUpcomingMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtToolbarTitle = findViewById(R.id.txt_title);
        txtToolbarTitle.setText(R.string.upcoming_movies);

        recyclerUpcomingMovie = findViewById(R.id.recycler_upcoming_movies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerUpcomingMovie.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUpcomingMovies();
    }

    private void getUpcomingMovies() {
        showLoadingDialog();
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {

            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET, ApiConstants.UPCOMING_MOVIES_URL,
                    ApiConstants.UPCOMING_MOVIES_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.UPCOMING_MOVIES_URL);

        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info_screen:
                Intent intent = new Intent(mContext, InformationActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.UPCOMING_MOVIES_URL: {
                if (jsonResponse != null) {

                    UpcomingMovieAdapter upcomingMovieAdapter = new UpcomingMovieAdapter(mContext, jsonResponse.results);
                    recyclerUpcomingMovie.setAdapter(upcomingMovieAdapter);
                }
                break;
            }
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.UPCOMING_MOVIES_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.UPCOMING_MOVIES_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
