package com.example.priyandubey.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.priyandubey.musicplayer.MainActivity.countDownTimer;
import static com.example.priyandubey.musicplayer.MainActivity.mediaPlayer;
import static com.example.priyandubey.musicplayer.MainActivity.progressBar;
import static com.example.priyandubey.musicplayer.MainActivity.seekBar;
import static com.example.priyandubey.musicplayer.MainActivity.status;
import static com.example.priyandubey.musicplayer.MainActivity.textSongName;
import static com.example.priyandubey.musicplayer.MainActivity.timerProg;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public List<MusicInfo> music;
    private Context mContext;
    public RecyclerAdapter(List<MusicInfo> music, Context mContext) {
        this.music = music;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.fname.setText(music.get(position).musicName);
        holder.fAlbum.setText(music.get(position).musicAlbum);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                try {
                    Uri myUri = music.get(position).musicResourceUri;
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(mContext, myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    textSongName.setText(" " + music.get(position).musicName);

                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(),Context.MODE_PRIVATE);
                    sharedPreferences.edit().putInt("pos",position).apply();
                    status = 1;
                    setTimer(music.get(position).musicDuration,0);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return music.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fname;
        TextView fAlbum;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            fname = itemView.findViewById(R.id.mName);
            fAlbum = itemView.findViewById(R.id.mAlbum);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public  void setTimer(long timer,int curr){

        progressBar.setMax(convert(timer));
        progressBar.setProgress(curr);
        timerProg = 0;
        Log.i("from the method-",Integer.toString(convert(timer)));
        progressBar.setProgress(0);
        countDownTimer = new CountDownTimer(timer,1000) {
            @Override
            public void onTick(long l) {
              //  Log.i("timer progress",Integer.toString(timerProg));
                timerProg++;
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
       // Log.i("duration : -",s);
        return Integer.parseInt(s);
    }


}