package com.upcomingmovieapp.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.upcomingmovieapp.R;
import com.upcomingmovieapp.model.UpcomingMoviesModel;
import com.upcomingmovieapp.webservice.ApiConstants;

import java.util.ArrayList;

public class ImageSlidingAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<UpcomingMoviesModel> mUpcomingMoviesModels;
    private LayoutInflater layoutInflater;

    public ImageSlidingAdapter(Context context, ArrayList<UpcomingMoviesModel> upcomingMoviesModels) {
        this.mContext = context;
        this.mUpcomingMoviesModels = upcomingMoviesModels;
    }

    @Override
    public int getCount() {
        if (mUpcomingMoviesModels != null && mUpcomingMoviesModels.size() > 0)
            return mUpcomingMoviesModels.size();
        else
            return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_layout, null);

        ImageView imageView = view.findViewById(R.id.imageView);

        try {
            if (mUpcomingMoviesModels.get(position).file_path != null || mUpcomingMoviesModels.get(position).file_path.isEmpty()) {
                Picasso.with(mContext)
                        .load(ApiConstants.IMAGE_URL + mUpcomingMoviesModels.get(position).file_path)
                        .error(R.drawable.default_image)
                        .into(imageView);
            }
        } catch (Exception e) {
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}