package com.upcomingmovieapp.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class CommonUtils {

    private static CommonUtils nUtilHelper;

    public static CommonUtils getInstance(Context context) {
        if (nUtilHelper == null) {
            nUtilHelper = new CommonUtils();
        }
        return nUtilHelper;
    }

    public boolean checkConnectivity(Context pContext) {
        ConnectivityManager lConnectivityManager = (ConnectivityManager)
                pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo lNetInfo = lConnectivityManager.getActiveNetworkInfo();
        return lNetInfo != null && lNetInfo.isConnected();
    }

    public static void showSnack(Context context, String text) {
        Snackbar snack = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), text , Snackbar.LENGTH_SHORT);
        snack.setDuration(Snackbar.LENGTH_LONG);//change Duration as you need
        //snack.setAction(actionButton, new View.OnClickListener());//add your own listener
        View view = snack.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);//change textColor

        TextView tvAction = view.findViewById(android.support.design.R.id.snackbar_action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.WHITE);

        snack.show();
    }
}
