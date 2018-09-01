package com.example.android.musicpanda.MusicPanda.Fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.musicpanda.MusicPanda.ClickListener;
import com.example.android.musicpanda.MusicPanda.MusicAdapter;
import com.example.android.musicpanda.MusicPanda.Panda;
import com.example.android.musicpanda.MusicPanda.PandaModel;
import com.example.android.musicpanda.MusicPanda.PlayService;
import com.example.android.musicpanda.MusicPanda.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MusicListFragment extends Fragment{

    private TextView noContentText;
    private RecyclerView mSongsList;
    public LiveData<ArrayList<File>> mMusicFiles;
    private MusicAdapter mAdapter;

    public MusicListFragment(){ }   //Mandatory Constructor




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_list_frag,container, false);
        mSongsList = (RecyclerView) rootView.findViewById(R.id.rv_song_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongsList.setLayoutManager(layoutManager);
        mSongsList.setHasFixedSize(true);

        // Get the No Content textview
        noContentText = (TextView) rootView.findViewById(R.id.no_content);


        mMusicFiles = ViewModelProviders.of(getActivity()).get(PandaModel.class).getFiles();


        //LiveData for MusicListFragment to observe
        mMusicFiles.observe(this, new Observer<ArrayList<File>>() {
            @Override
            public void onChanged(@Nullable ArrayList<File> files) {
                Panda.log("LiveData Onchange was called");
                Toast.makeText(getActivity(), "Total number of songs: " + files.size(), Toast.LENGTH_LONG).show();
                updateRecyclerView(files);

            }
        });

        return rootView;
    }


    public void showContent(){
        noContentText.setVisibility(View.GONE);
        mSongsList.setVisibility(View.VISIBLE);
    }

    public void showNoContentMessage(){
        noContentText.setVisibility(View.VISIBLE);
        mSongsList.setVisibility(View.INVISIBLE);

    }

    public void updateRecyclerView(ArrayList files){

        if (files != null && files.size() <= 0){
            showNoContentMessage();
            return;
        }
        else{
            showContent();
        }

        mAdapter = new MusicAdapter(transformFilesToNames(files), new ClickListener() {
            @Override
            public void clicked(int position) {
                // Play music with Uri
                String musicPath = mMusicFiles.getValue().get(position).getAbsolutePath();
                Uri songUri = Uri.parse(musicPath);
                Intent playIntent = new Intent(getActivity().getBaseContext(), PlayService.class);
                playIntent.setAction(PlayService.PLAY_MUSIC);
                playIntent.setData(songUri);
                playIntent.putExtra(PlayService.POSITION_KEY, position);
                getActivity().startService(playIntent);

                // TODO Start the detailActivity with an Intent that Contatins The Uri for the File


            }
        }); // This implements OnClickListener for the Adapter
        mSongsList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    public List<String> transformFilesToNames(ArrayList<File> files){
        //This method recieves an ArraList and returns a List of Strings
        ArrayList<String> list = new ArrayList<>();
        for (File file : files) {
            list.add(file.getName());
        }
        return list;
    }

}
