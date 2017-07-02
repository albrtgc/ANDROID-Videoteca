package com.alberto.videoteca;

import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.annotation.Target;

/**
 * Created by Alberto on 02/07/2017.
 */

public class FragmentDetalles extends DetailsFragment {

    private Movie mSelectedMovie;
    private Target mBackgroundTarget;
    private DisplayMetrics mMetrics;
    private static final String MOVIE = "Movie";
    private BackgroundManager mBackgroundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackground();
        mSelectedMovie = (Movie) getActivity().getIntent()
                .getSerializableExtra(MOVIE);
        updateBackground(mSelectedMovie.getBackgroundImageURI().toString());
    }
    private void initBackground() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }
    protected void updateBackground(String uri) {
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>(mMetrics.widthPixels,
                        mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
    }
}