package com.upcomingmovieapp.webservice;


public class ApiConstants {


    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    public static int LOG_STATUS = 0; // Change to 1 for production app

    public static final String SHARED_PREF = "UpcomingMovieApp";

    public static final String UPCOMING_MOVIES_URL = BASE_URL + "upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f";

    public static final String MOVIE_DETAIL_URL = "?api_key=b7cd3340a794e5a2f35e3abb820b497f";

    public static final String MOVIE_DETAIL_IMAGES_URL = "/images?api_key=b7cd3340a794e5a2f35e3abb820b497f";

}
