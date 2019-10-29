package com.example.priyandubey.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static com.example.priyandubey.musicplayer.MainActivity.cast;
import static com.example.priyandubey.musicplayer.MainActivity.countDownTimer;
import static com.example.priyandubey.musicplayer.MainActivity.mediaPlayer;
import static com.example.priyandubey.musicplayer.MainActivity.music;
import static com.example.priyandubey.musicplayer.MainActivity.progressBar;
import static com.example.priyandubey.musicplayer.MainActivity.textSongName;
import static com.example.priyandubey.musicplayer.MainActivity.timerProg;

public class MusicBroadcast extends BroadcastReceiver {

    SharedPreferences sharedPreferences;
    public  int status = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);

        if("com.example.priyandubey.MusicPlayer.prev".equals(intent.getAction())){

            if(countDownTimer != null)
                countDownTimer.cancel();
            int pos = sharedPreferences.getInt("pos",0);
            pos--;
            if(pos < 0) pos = music.size() - 1;
            status = 1;
           // Log.i("fromthebroadcastreciver"," " + music.get(pos).musicName);

            try {
                Uri myUri = music.get(pos).musicResourceUri;
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(context.getApplicationContext(), myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                textSongName.setText(" " + music.get(pos).musicName);
                timerProg = 0;
                setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);

                Intent callIntent = new Intent(context,MusicService.class);
                callIntent.putExtra("positionNotify",pos);
                context.startService(callIntent);

                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
                sharedPreferences.edit().putInt("pos",pos).apply();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if("com.example.priyandubey.MusicPlayer.play".equals(intent.getAction())){

            int pos = sharedPreferences.getInt("pos",0);

            if(status == 1) {
                countDownTimer.cancel();
                mediaPlayer.pause();
                status = 0;
            }else{
                setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);
                mediaPlayer.start();
                status = 1;
            }

        }
        if("com.example.priyandubey.MusicPlayer.next".equals(intent.getAction())){

            if(countDownTimer != null)
                countDownTimer.cancel();
            int pos = sharedPreferences.getInt("pos",0);
            pos++;
            if(pos == music.size()) pos = 0;
            status = 1;
            Log.i("fromthebroadcastreciver"," " + music.get(pos).musicName);
            try {
                Uri myUri = music.get(pos).musicResourceUri;
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(context, myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                textSongName.setText(" " + music.get(pos).musicName);
                timerProg = 0;
                setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);

                Intent callIntent = new Intent(context,MusicService.class);
                callIntent.putExtra("positionNotify",pos);
                context.startService(callIntent);

                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
                sharedPreferences.edit().putInt("pos",pos).apply();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if("com.example.priyandubey.MusicPlayer.cancel".equals(intent.getAction())){

            Log.i("fromthebroadcastreciver","cancel");
            Intent mintent = new Intent(context, MusicService.class);
            context.stopService(mintent);
            mediaPlayer.stop();
            mediaPlayer.reset();
            context.unregisterReceiver(cast);

        }

    }

    public  void setTimer(long timer,int curr,long dur){

        progressBar.setMax(convert(dur));
        progressBar.setProgress(curr);
        countDownTimer = new CountDownTimer(timer,1000) {
            @Override
            public void onTick(long l) {
                //   Log.i("timer progress",Integer.toString(timerProg));
                ++timerProg;
                progressBar.incrementProgressBy(1);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                countDownTimer.cancel();
            }
        }.start();

    }
    public int convert(long l){
        l/=1000;
        String s = Long.toString(l);
        //Log.i("duration : -",s);
        return Integer.parseInt(s);
    }

}
