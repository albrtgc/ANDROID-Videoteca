package com.alberto.videoteca;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.alberto.videoteca.activity.DetailsActivity;
import com.alberto.videoteca.models.Movie;
import com.alberto.videoteca.models.MovieList;

import java.io.IOException;
import java.util.List;

/**
 * Created by Alberto on 02/07/2017.
 */

public class UpdateRecommendationsService extends IntentService {

    private static final int MAX_RECOMMENDATIONS = 3;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;
    private NotificationManager mNotificationManager;

    public UpdateRecommendationsService() {
        super("RecommendationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Movie> recommendations = MovieList.list;
        if (recommendations == null) return;
        if (mNotificationManager==null){
            mNotificationManager=(NotificationManager)
                    getApplicationContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        int count = 0;
        try {
            RecommendationBuilder builder = new RecommendationBuilder().setContext(getApplicationContext()).setSmallIcon(R.drawable.videos_by_google_icon);
            for( Movie movie : MovieList.list ) {
                builder.setBackground(movie.getCardImageUrl())
                        .setId(count+1)
                        .setPriority(MAX_RECOMMENDATIONS - count)
                        .setTitle(movie.getTitle())
                        .setDescription(getString(R.string.popular_header))
                        .setImage(movie.getCardImageUrl())
                        .setIntent(buildPendingIntent(movie))
                        .build();
                Notification notification = builder.build();
                mNotificationManager.notify(count+1, notification);
                if (++count >= MAX_RECOMMENDATIONS) {
                    break;
                }
            }
        } catch (IOException e) {
        }
    }
    private PendingIntent buildPendingIntent(Movie movie) {
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra("Movie", movie);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(detailsIntent);
        detailsIntent.setAction(Long.toString(movie.getId()));
        PendingIntent intent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return intent;
    }
}