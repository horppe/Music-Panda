package com.example.android.musicpanda.MusicPanda;

import android.app.IntentService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.TimeUtils;
import android.util.Log;


public class PlayService extends Service {
    //Sent out Intents
    public static final String ACTION_SEND_DURATION = "action-send-duration";
    public static final String DURATION_KEY = "duration-key";

    public static final int SECOND_DIV = 1700;

    public static final String UPDATE_PROGRESS_KEY = "update";
    public static final String UPDATE_ACTION = "update-action";

    // Playback controls Constant
    public static final String PLAY_MUSIC = "play-music";
    public static final String PAUSE_MUSIC ="pause-music";
    public static final String NEXT_MUSIC = "next-music";
    public static final String PREVIOUS_MUSIC = "previous-music";
    public static final String POSITION_KEY = "position-key";
    public static final String PLAY_OR_PAUSE = "play-or-pause";
    public static final String UPDATE_MUSIC = "update-music";
    public static final String UPDATE_MUSIC_POSITION = "update-music-position";
    public static final int POSITION_DEFAULT = 0;

    public final static MediaPlayer mPlayer = new MediaPlayer();

    public static int currentPlayPosition;

    public PlayService(){

    }

    class PlayServiceThread extends Thread{

        public Handler mHandler;


        public PlayServiceThread(){

        }

        @Override
        public void run() {
            Log.d("TestService", "Inside run");
            Looper.prepare();
            mHandler = new Handler();
            Looper.loop();
        }
    }

    public PlayServiceThread mThread = new PlayServiceThread();


    @Override
    public void onCreate() {
        super.onCreate();
        // All the setup that will happen once
        mThread.start();



    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        loop();

        // TODO Check if intent.getAction is not null, then call HandleIntent with the intent;

        if (intent.getAction() != null) {
            mThread.mHandler.postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    onHandleIntent(intent);
                }
            });
        }

        return START_STICKY;
    }

    void loop(){
        // TODO if this causes an error move it back inside start Command
            mThread.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Runnable", "Run is being called");
                    //First check if the player is playing before sending the broadcastIntent to Reciever inside SeekBarControl
                    if (mPlayer.isPlaying()){
                        int position = mPlayer.getCurrentPosition() / SECOND_DIV;
                        Intent updateIntent = new Intent();
                        updateIntent.putExtra(UPDATE_PROGRESS_KEY, position);
                        updateIntent.setAction(UPDATE_ACTION);
                        sendBroadcast(updateIntent);
                        Log.d("Broadcast", "Current postion Broadcast sent to Receiver = " + position);
                    }
                    mThread.mHandler.postDelayed(this, 2000);
                }
            });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    protected void onHandleIntent(@Nullable Intent intent) {
        Panda.log("PlayService onHandleIntent was called");
        String action = intent.getAction();

        switch (action){
            case PLAY_MUSIC:
                Panda.log("PlayMusic action was called");
                Uri playUri = intent.getData();
                int position = intent.getIntExtra(POSITION_KEY, POSITION_DEFAULT);
                playMusic(playUri, position);
                break;
            case PAUSE_MUSIC: pause();
                break;

            case PLAY_OR_PAUSE: playOrPause();
                break;
            case NEXT_MUSIC:
                Uri nextSong = intent.getData();
                next(nextSong);
                break;
            case PREVIOUS_MUSIC:
                Uri prevSong = intent.getData();
                previous(prevSong);
                break;
            case UPDATE_MUSIC:
                int currentPos = intent.getIntExtra(UPDATE_MUSIC_POSITION, -1);
                if (currentPos != -1)
                    mPlayer.seekTo(currentPos);
                defaul:
                    break;
        }
    }

    void play(){
        mPlayer.start();
    }
    void pause(){
        mPlayer.pause();
    }

    void playOrPause(){
        if (mPlayer == null)
            return;

        if (mPlayer.isPlaying())
            pause();
        else
            play();
    }


    private void playMusic(Uri file, int position){
        if (true) {
            mPlayer.stop();
            mPlayer.reset();
            //Dont ever set Media player to null again
            // just call its reset method
        }


        try{
            mPlayer.setDataSource(getBaseContext(), file);
            mPlayer.prepare();
            sendDurationToSeekBar();
            mPlayer.start();

            //To aid in incrementing to the next song
            currentPlayPosition = position;
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    void sendDurationToSeekBar(){
         int duration = mPlayer.getDuration() / SECOND_DIV;
            Log.d("Runnable", "Duration update is being called");
            //First check if the player is playing before sending the broadcastIntent to Reciever inside SeekBarControl
        Intent updateIntent = new Intent();
        updateIntent.putExtra(DURATION_KEY, duration);
        updateIntent.setAction(ACTION_SEND_DURATION);
        sendBroadcast(updateIntent);
        Log.d("Broadcast", "Duration sent to Receiver = " + duration);
    }

    void next( Uri nextSong) {
            int nextPosition = currentPlayPosition + 1;
            playMusic(nextSong, nextPosition);
    }
    void previous(Uri prevSong) {
            int prevPosition = currentPlayPosition - 1;
            playMusic(prevSong, prevPosition);
    }

}
