package com.example.priyandubey.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    static ProgressBar progressBar;
    static public int timerProg;
    static CountDownTimer countDownTimer;
    public static int status = 0;
    public static MediaPlayer mediaPlayer;
    public static TextView textSongName;
//    public static
   // public int t = timerProg;
    public static MusicBroadcast cast = new MusicBroadcast();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.songs :
                    selectedFragment = new SongFragment();
                    break;
                case R.id.games:
                    selectedFragment = new GameFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };


    final int PERMISSION_READ_EXTERNAL = 1;
    static SeekBar seekBar;
    public static ArrayList<MusicInfo> music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        progressBar = findViewById(R.id.progressBar);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mediaPlayer = new MediaPlayer();
        sharedPreferences = this.getSharedPreferences(getPackageName(),MODE_PRIVATE);
        textSongName = findViewById(R.id.textSongName);
        music = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL);
        }else{
            loadMusic();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SongFragment()).commit();

        IntentFilter pintentFilter = new IntentFilter("com.example.priyandubey.MusicPlayer.prev");
        registerReceiver(cast,pintentFilter);

        IntentFilter qintentFilter = new IntentFilter("com.example.priyandubey.MusicPlayer.play");
        registerReceiver(cast,qintentFilter);

        IntentFilter rintentFilter = new IntentFilter("com.example.priyandubey.MusicPlayer.next");
        registerReceiver(cast,rintentFilter);

        IntentFilter sintentFilter = new IntentFilter("com.example.priyandubey.MusicPlayer.cancel");
        registerReceiver(cast,sintentFilter);


        Button pausePlay = findViewById(R.id.pausePlay);
        Button prevPlay = findViewById(R.id.prevPlay);
        Button nextPlay = findViewById(R.id.nextPlay);
        prevPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(countDownTimer != null)
                    countDownTimer.cancel();
                int pos = sharedPreferences.getInt("pos",0);
                pos--;
                if(pos < 0) pos = music.size() - 1;

                try {
                    Uri myUri = music.get(pos).musicResourceUri;
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    textSongName.setText(" " + music.get(pos).musicName);
                    timerProg = 0;
                    setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(),MODE_PRIVATE);
                    sharedPreferences.edit().putInt("pos",pos).apply();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
        nextPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(countDownTimer != null)
                try {
                    countDownTimer.cancel();
                }catch (Exception e){
                    e.printStackTrace();
                }
                int pos = sharedPreferences.getInt("pos",0);
                pos++;
                if(pos == music.size()) pos = 0;
                try {
                    Uri myUri = music.get(pos).musicResourceUri;
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    textSongName.setText(" " + music.get(pos).musicName);
                    timerProg = 0;
                    setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(),MODE_PRIVATE);
                    sharedPreferences.edit().putInt("pos",pos).apply();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int pos = sharedPreferences.getInt("pos",0);
                pos++;
                if(pos == music.size()) pos = 0;
                try {
                    Uri myUri = music.get(pos).musicResourceUri;
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    textSongName.setText(" " + music.get(pos).musicName);
                    timerProg = 0;
                    setTimer(music.get(pos).musicDuration - timerProg*1000l,timerProg,music.get(pos).musicDuration);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(),MODE_PRIVATE);
                    sharedPreferences.edit().putInt("pos",pos).apply();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSION_READ_EXTERNAL :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMusic();
                }
        }
    }

    public void loadMusic(){

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();

            for(int x = 0;x<cursor.getCount();x++){
                cursor.moveToPosition(x);

                int i1 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int i2 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                int i3 = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                long fd = Long.parseLong(cursor.getString(i3));

                MusicInfo musicInfo = new MusicInfo(Uri.parse(cursor.getString(1)),cursor.getString(i1),cursor.getString(i2),fd);
                music.add(musicInfo);


            }

        }

        try {
            int j = sharedPreferences.getInt("pos",0);
            textSongName.setText(music.get(j).musicName);
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), music.get(j).musicResourceUri);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
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
        Log.i("duration : -",s);
        return Integer.parseInt(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        unregisterReceiver(cast);
        Intent mintent = new Intent(this, MusicService.class);
        stopService(mintent);
    }
}
