package com.example.priyandubey.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static int status = 0;
    public static MediaPlayer mediaPlayer;
    public static TextView textSongName;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.songs :
                    selectedFragment = new SongFragment();
                    break;
                case R.id.favourites:
                    selectedFragment = new FavouriteFragment();
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

    public static ArrayList<MusicInfo> music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mediaPlayer = new MediaPlayer();
        sharedPreferences = this.getSharedPreferences(getPackageName(),MODE_PRIVATE);
        textSongName = findViewById(R.id.textSongName);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SongFragment()).commit();

        music = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL);
        }else{
            loadMusic();
        }

        Button pausePlay = findViewById(R.id.pausePlay);
        Button prevPlay = findViewById(R.id.prevPlay);
        Button nextPlay = findViewById(R.id.nextPlay);
        prevPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    mediaPlayer.pause();
                    status = 0;
                }else{
                    mediaPlayer.start();
                    status = 1;
                }
            }
        });
        nextPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                MusicInfo musicInfo = new MusicInfo(Uri.parse(cursor.getString(1)),cursor.getString(i1),cursor.getString(i2));
             //   Log.i("songs :- ",Uri.parse(cursor.getString(1)) + " " + cursor.getString(i1) + " " + cursor.getString(i2));
                music.add(musicInfo);

            }

        }
        textSongName.setText(music.get(0).musicName);
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), music.get(0).musicResourceUri);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
