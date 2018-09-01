package com.example.android.musicpanda.MusicPanda.Receivers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.android.musicpanda.MusicPanda.PlayService;

public class Receiver extends BroadcastReceiver {

    public interface UpdateBar{
        void setValue(int value);
        void setDuration(int max);
    }
    UpdateBar mainActivity;
    public Receiver(){

    }
    public Receiver(UpdateBar activity){
        mainActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case PlayService.UPDATE_ACTION:
                int updatedCount = intent.getIntExtra(PlayService.UPDATE_PROGRESS_KEY, -1);
                if (updatedCount != -1){
                    Log.d("Receiver", "Intent succesfully received from the Service");
                    Log.d("Receiver", "calling MainActivity interface to update Views = " + updatedCount);

                   mainActivity.setValue(updatedCount);
                }else{
                  Log.d("Failure", "the count is not accurate");
                }
            break;
            case PlayService.ACTION_SEND_DURATION:
                // SET THE DURATION TO THE VALUE FROM THE INTENT
                int duration = intent.getIntExtra(PlayService.DURATION_KEY, -1);
                Toast.makeText(context, "Duration received in Receiver", Toast.LENGTH_SHORT).show();
                Log.d("In Receiver", "Duration was received = " + duration);

                if (duration > -1) {
                    mainActivity.setDuration(duration);
                    Log.d("Duration", "Duration set");
                }

            break;
            default: break;

        }
    }
}
