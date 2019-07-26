package com.example.priyandubey.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.priyandubey.musicplayer.MainActivity.mediaPlayer;
import static com.example.priyandubey.musicplayer.MainActivity.textSongName;

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

}
