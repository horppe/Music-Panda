package com.example.android.musicpanda.MusicPanda.Fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.android.musicpanda.MusicPanda.PlayService;
import com.example.android.musicpanda.MusicPanda.R;
import com.example.android.musicpanda.MusicPanda.Receivers.Receiver;

public class SeekBarControl extends Fragment implements SeekBar.OnSeekBarChangeListener, Receiver.UpdateBar{

    SeekBar mSeekbar;

    Receiver mReceiver;

    public SeekBarControl(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView= inflater.inflate(R.layout.seekbar_frag, container, false);

        mSeekbar = (SeekBar) myView.findViewById(R.id.seekBar);

        mSeekbar.setOnSeekBarChangeListener(this);

        // Register the Reciever by first passing it this.UpdateBar interface
        // and then register the receiver with the host activity;
        mReceiver = new Receiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlayService.UPDATE_ACTION);

        getActivity().registerReceiver(mReceiver, intentFilter);

        return myView;
    }

    public void setValue(int value){
        mSeekbar.setProgress(value);
    }

    @Override
    public void setDuration(int duration){
        mSeekbar.setMin(0);
        mSeekbar.setMax(duration);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser == true) {
            // Send new intent with Action.UpdatePlayer with the current position
            Intent playIntent = new Intent(getActivity().getBaseContext(), PlayService.class);
            playIntent.setAction(PlayService.UPDATE_MUSIC);
            playIntent.putExtra(PlayService.UPDATE_MUSIC_POSITION, progress * PlayService.SECOND_DIV);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }



}
