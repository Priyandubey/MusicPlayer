package com.example.priyandubey.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static com.example.priyandubey.musicplayer.MainActivity.countDownTimer;
import static com.example.priyandubey.musicplayer.MainActivity.mediaPlayer;
import static com.example.priyandubey.musicplayer.MainActivity.music;
import static com.example.priyandubey.musicplayer.MainActivity.progressBar;
import static com.example.priyandubey.musicplayer.MainActivity.textSongName;
import static com.example.priyandubey.musicplayer.MainActivity.timerProg;
import static com.example.priyandubey.musicplayer.MusicService.notification;

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


                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
                sharedPreferences.edit().putInt("pos",pos).apply();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        if("com.example.priyandubey.MusicPlayer.play".equals(intent.getAction())){

            if(status == 1) {
                Log.i("timer progress 1",Integer.toString(timerProg));
                countDownTimer.cancel();
                mediaPlayer.pause();
                status = 0;
            }else{
                Log.i("timer progress 2",Integer.toString(timerProg));
                int pos = sharedPreferences.getInt("pos",0);
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


                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
                sharedPreferences.edit().putInt("pos",pos).apply();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if("com.example.priyandubey.MusicPlayer.cancel".equals(intent.getAction())){

            Intent mintent = new Intent(context, MusicService.class);
            mediaPlayer.stop();
            mediaPlayer.reset();
            context.stopService(mintent);

        }

    }

    public  void setTimer(long timer,int curr,long dur){

        progressBar.setMax(convert(dur));
        progressBar.setProgress(curr);
        // Log.i("duration req",Integer.toString(convert(dur)));
        // Log.i("should play from-",Integer.toString(curr));
        //progressBar.setProgress(0);
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
