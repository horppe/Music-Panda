package com.example.android.musicpanda.MusicPanda;


import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.musicpanda.MusicPanda.Fragments.MusicListFragment;
import com.example.android.musicpanda.MusicPanda.Fragments.PlaybackControlFragment;
import com.example.android.musicpanda.MusicPanda.Receivers.Receiver;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Registering Broadcast Receiver


        //Panda needs to be setup first
        Panda.setUp(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        MusicListFragment musicListFragment = new MusicListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.recycler_view_frag, musicListFragment)
                .commit();

        PlaybackControlFragment playbackControlFragment = new PlaybackControlFragment();

        fragmentManager.beginTransaction()
                .add(R.id.buttons_container, playbackControlFragment)
                .commit();
        startService(new Intent(this, PlayService.class));
    }

}
