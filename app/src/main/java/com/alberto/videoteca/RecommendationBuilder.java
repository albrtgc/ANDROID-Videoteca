package com.alberto.videoteca;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Alberto on 02/07/2017.
 */

public class RecommendationBuilder {

    private String mTitle;
    private String mDescription;
    private String mImageUri;
    private String mBackgroundUri;
    private Integer mPriority;
    private Bitmap image;
    private int mSmallIcon;
    private PendingIntent mIntent;
    private Context mContext;
    private int mId;
    private Bundle extras;

    public RecommendationBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }
    public RecommendationBuilder setDescription(String description) {
        mDescription = description;
        return this;
    }
    public RecommendationBuilder setId(Integer id) {
        mId = id;
        return this;
    }
    public RecommendationBuilder setContext(Context context) {
        mContext = context;
        return this;
    }
    public RecommendationBuilder setPriority(Integer priority) {
        mPriority = priority;
        return this;
    }
    public RecommendationBuilder setImage(String uri) {
        mImageUri = uri;
        image = loadBitmap(mImageUri);
        return this;
    }
    public RecommendationBuilder setSmallIcon(Integer smallIcon) {
        mSmallIcon = smallIcon;
        return this;
    }
    public RecommendationBuilder setIntent(PendingIntent intent) {
        mIntent = intent;
        return this;
    }
    public RecommendationBuilder setBackground(String uri) {
        mBackgroundUri = uri;
        return this;
    }
    public Notification build() throws IOException {
        Notification notification = new NotificationCompat.BigPictureStyle(
                new NotificationCompat.Builder(mContext)
                        .setContentTitle(mTitle)
                        .setContentText(mDescription)
                        .setPriority(mPriority)
                        .setLocalOnly(true)
                        .setOngoing(true)
                        .setColor(mContext.getResources().getColor(
                                R.color.fastlane_background))
                        .setCategory(Notification.CATEGORY_RECOMMENDATION)
                        .setLargeIcon(image)
                        .setSmallIcon(mSmallIcon)
                        .setContentIntent(mIntent)
                        .setExtras(extras))
                .build();
        return notification;
    }
    private Bitmap loadBitmap(String url){
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }
}