package com.example.priyandubey.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.example.priyandubey.musicplayer.App.CHANNEL_ID;
import static com.example.priyandubey.musicplayer.MainActivity.mediaPlayer;
import static com.example.priyandubey.musicplayer.MainActivity.music;

public class MusicService extends Service {

    public static Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int position = intent.getIntExtra("positionNotify",0);
        Log.i("In the service--",Integer.toString(position));

        try {

            Uri myUri = music.get(position).musicResourceUri;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent mintent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,mintent,0);


        Intent pintent = new Intent("com.example.priyandubey.MusicPlayer.prev") ;
        PendingIntent ppendingIntent = PendingIntent.getBroadcast(this,0,pintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent qintent = new Intent("com.example.priyandubey.MusicPlayer.play");
        PendingIntent qpendingIntent = PendingIntent.getBroadcast(this,0,qintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent rintent = new Intent("com.example.priyandubey.MusicPlayer.next");
        PendingIntent rpendingIntent = PendingIntent.getBroadcast(this,0,rintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent sintent = new Intent("com.example.priyandubey.MusicPlayer.cancel");
        PendingIntent spendingIntent = PendingIntent.getBroadcast(this,0,sintent,PendingIntent.FLAG_UPDATE_CURRENT);



        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(music.get(position).musicName)
                .setContentText(music.get(position).musicAlbum)
                .setSmallIcon(R.drawable.mimage)
                .setLargeIcon(convertBitmap(position))
                .setContentIntent(null)
                .addAction(R.drawable.notifyprev,"prev",ppendingIntent)
                .addAction(R.drawable.notifyplay,"play",qpendingIntent)
                .addAction(R.drawable.notifynext,"next",rpendingIntent)
                .addAction(R.drawable.notifycancel,"cancel",spendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2))
                .build();

        startForeground(1,notification);

        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Bitmap convertBitmap(int position){
            Bitmap art;
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            byte[] rawArt;
            BitmapFactory.Options bfo = new BitmapFactory.Options();

            mmr.setDataSource(this, music.get(position).getMusicResourceUri());
            rawArt = mmr.getEmbeddedPicture();

            if (null != rawArt) {
                art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
            }else{
                art = BitmapFactory.decodeResource(this.getResources(),R.drawable.mimage);
            }

        return art;
    }


}
