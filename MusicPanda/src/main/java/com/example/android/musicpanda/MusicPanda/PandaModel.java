package com.example.android.musicpanda.MusicPanda;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PandaModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<File>> mMusicFiles;


    public PandaModel(@NonNull Application application) {
        super(application);
        mMusicFiles = new MutableLiveData<>();
        scanDirectory();
    }

    public LiveData<ArrayList<File>> getFiles(){
        return mMusicFiles;
    }

    private void scanDirectory(){
        // PathName is left hardcoded for flexibility to switch to either one storage or all
        final File root = new File("/storage");
        Panda.log("About to start walking the Tree");

        new AsyncTask<Void, Void, List<String>>(){
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> newlist = new ArrayList<>();
                traverseDir(root, newlist);
                return newlist;
            }

            @Override
            protected void onPostExecute(List<String> resultList) {
                super.onPostExecute(resultList);

                ArrayList<File> tempFiles = new ArrayList<>();
                for (String path: resultList) {
                     tempFiles.add(new File(path));
                }
                mMusicFiles.setValue(tempFiles);
                this.cancel(true);
            }
        }.execute();
    }

    // To play music from Uri
    // Needs further refactoring


    public void traverseDir(File file, List<String> tempList){
        File files[] = null;
        try{
            files = file.listFiles();

        }
        catch (Exception e){
            Log.d("There was an Error", "A File error occured");
            return;
        }
        if(files == null || files.length <= 0){
            return;
        }
        for (File fl: files) {
            String name = fl.getName();
            if (name.endsWith(".mp3") || name.endsWith(".m4a")){
                String fullPath = fl.getAbsolutePath().toString();
                Panda.log(fullPath);
                tempList.add(fullPath);
            }
            traverseDir(fl, tempList);
        }
    }


}
