package com.upcomingmovieapp.webservice;

import com.upcomingmovieapp.model.UpcomingMoviesModel;

import java.util.ArrayList;

public class JsonResponse {

    public String original_title;
    public String overview;
    public String popularity;
    public String vote_average;
    public String vote_count;

    public ArrayList<UpcomingMoviesModel> results;
    public ArrayList<UpcomingMoviesModel> backdrops;
}
