package com.upcomingmovieapp.webservice;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.upcomingmovieapp.interfaces.ApiServiceCaller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebRequests {

    private static final String TAG = "WebRequests";

    public static JsonObjectRequest callPostMethod(JSONObject jsonObject, int request_type, String url, final String label,
                                                   final ApiServiceCaller caller) {

        if (ApiConstants.LOG_STATUS == 0) {
            Log.d("Api_Calling", "" + url);
            Log.d("JSONObject", "" + jsonObject);
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request_type, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.i(TAG, response.toString());
                            Gson gson = new Gson();
                            JsonResponse jsonResponse = gson.fromJson(response.toString(), JsonResponse.class);
                            caller.onAsyncSuccess(jsonResponse, label);
                        } catch (Exception e) {
                            e.printStackTrace();
                            caller.onAsyncCompletelyFail("Failed", label);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        VolleyLog.d(TAG, "Error: " + res);
                        Gson gson = new Gson();
                        JsonResponse jsonResponse = gson.fromJson(res, JsonResponse.class);
                        caller.onAsyncSuccess(jsonResponse, label);
                    } catch (Exception je) {
                        caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);
                        je.printStackTrace();
                    }
                } else {
                    caller.onAsyncFail(error.getMessage() != null && !error.getMessage().equals("") ? error.getMessage() : "Please Contact Server Admin", label, response);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                return params;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(6000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsonObjReq;
    }
}



