package com.example.android.musicpanda.MusicPanda.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.android.musicpanda.MusicPanda.Panda;
import com.example.android.musicpanda.MusicPanda.PandaModel;
import com.example.android.musicpanda.MusicPanda.PlayService;
import com.example.android.musicpanda.MusicPanda.R;

import java.io.File;
import java.util.ArrayList;

public class PlaybackControlFragment extends Fragment{
    ImageButton mPrevBtn;
    ImageButton mNextBtn;
    ImageButton mPlayBtn;

    SeekBar mSeekBar;

    public  PlaybackControlFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = (View) inflater.inflate(R.layout.playback_control_frag, container, false);

        mPrevBtn = (ImageButton) rootView.findViewById(R.id.left_button);
        mNextBtn = (ImageButton) rootView.findViewById(R.id.right_button);
        mPlayBtn = (ImageButton) rootView.findViewById(R.id.play_button);

        initializeControlButtons();


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SeekBarControl seekBarControl = new SeekBarControl();
        getChildFragmentManager().beginTransaction()
                .add(R.id.seekBarContainer, seekBarControl).commit();

    }

    void initializeControlButtons(){
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri nextSong = getUriFromPosition(PlayService.currentPlayPosition + 1);
                if (nextSong != null)
                getActivity().startService(new Intent(getActivity(), PlayService.class)
                        .setAction(PlayService.NEXT_MUSIC)
                        .setData(nextSong));
            }
        });
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri prevSong = getUriFromPosition(PlayService.currentPlayPosition - 1);
                if (prevSong != null)
                getActivity().startService(new Intent(getActivity(), PlayService.class)
                        .setAction(PlayService.PREVIOUS_MUSIC)
                .setData(prevSong));
            }
        });

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), PlayService.class)
                        .setAction(PlayService.PLAY_OR_PAUSE));
            }
        });

        //To add callback to play next music after completion
        onCompletion();
    }

    Uri getUriFromPosition(int position){
        ArrayList<File> list = ViewModelProviders.of(getActivity())
                .get(PandaModel.class)
                .getFiles().getValue();
        if (position < 0 || position >= list.size())
            return null;
        Uri musicUri = Uri.parse(list
                .get(position).toString());
        return  musicUri;
    }

    // To trigger next music after completion
    void onCompletion(){
        PlayService.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Uri nextSong = getUriFromPosition(PlayService.currentPlayPosition + 1);
                if (nextSong != null)
                    getActivity().startService(new Intent(getActivity(), PlayService.class)
                            .setAction(PlayService.NEXT_MUSIC)
                            .setData(nextSong));
            }
        });
    }


}
