package com.goncanapp.videoteca.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.goncanapp.videoteca.models.Movie;

/**
 * Created by Alberto on 02/07/2017.
 */

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;

        if (movie != null) {
            viewHolder.getTitle().setText(movie.getTitle());
            viewHolder.getSubtitle().setText(movie.getStudio());
            viewHolder.getBody().setText(movie.getDescription());
        }
    }
}
