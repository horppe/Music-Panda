package com.example.android.musicpanda.MusicPanda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    TextView songDescription;
    ImageView songImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        songDescription = (TextView) findViewById(R.id.textView);
        songImage = (ImageView) findViewById(R.id.imageView);

            Toast.makeText(this, "Intent received from MainActivity", Toast.LENGTH_LONG).show();

    }
}
