package com.example.android.musicpanda.MusicPanda;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;




@TargetApi(23) public class Panda {

    public static final String DETAIL_ACTIVITY_KEY = "detail-activity-key";
    private static final int READ_EXTERNAL_STORAGE_CODE = 2345;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 59689;

    public static void log(String message){Log.d("Panda", message);}


    // This method give Panda Utility static Class the context that invoked it
    public static void setUp(AppCompatActivity context){
        getPermissions(context);
    }



    private static void getPermissions(AppCompatActivity mContext){

        boolean permission = (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);


        Log.d("Permission is", " " + permission);

        if (!permission){
            mContext.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
    }
}
