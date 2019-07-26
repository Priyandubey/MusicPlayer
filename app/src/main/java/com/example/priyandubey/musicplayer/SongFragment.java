package com.example.priyandubey.musicplayer;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.priyandubey.musicplayer.MainActivity.music;


public class SongFragment extends Fragment {


    public SongFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_song, container, false);

        recyclerView = view.findViewById(R.id.recyclerSong);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter(music,getContext());
        recyclerView.setAdapter(recyclerAdapter);




        return view;
    }

}
