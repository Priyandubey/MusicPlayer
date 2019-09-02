package com.example.priyandubey.musicplayer;

import android.net.Uri;
import android.widget.ImageView;

public class MusicInfo {

    public Uri musicResourceUri;
    public String musicName;
    public String musicAlbum;
    public long musicDuration;
  //  public ImageView musicImage;

    public MusicInfo(Uri musicResourceUri, String musicName, String musicAlbum,long musicDuration) {
        this.musicResourceUri = musicResourceUri;
        this.musicName = musicName;
        this.musicAlbum = musicAlbum;
        this.musicDuration = musicDuration;
       // this.musicImage = musicImage;
    }

    public Uri getMusicResourceUri() {
        return musicResourceUri;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public long getMusicDuration() {
        return musicDuration;
    }

//    public ImageView getMusicImage(){
//        return musicImage;
//    }

    /* public long getMusicDuration() {
        return musicDuration;
    }
    public ImageView getMusicImage() {
        return musicImage;
    }*/
}
