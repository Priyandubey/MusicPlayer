package com.example.priyandubey.musicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "playerNotification";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();
    }

    public void createNotification() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "musicNotify",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.enableVibration(false);
        channel.setSound(null,null);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

    }


}
