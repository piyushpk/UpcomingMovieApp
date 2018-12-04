package com.upcomingmovieapp.interfaces;

import com.android.volley.NetworkResponse;
import com.upcomingmovieapp.webservice.JsonResponse;

public interface ApiServiceCaller {

    void onAsyncSuccess(JsonResponse jsonResponse, String label);

    void onAsyncFail(String message, String label, NetworkResponse response);

    void onAsyncCompletelyFail(String message, String label);
}
