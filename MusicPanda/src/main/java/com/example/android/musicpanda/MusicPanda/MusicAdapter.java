package com.example.android.musicpanda.MusicPanda;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private List<String> mSongNames;
    private ClickListener mClickListener;

    public MusicAdapter(List<String> songNames, ClickListener listener){
        mSongNames = songNames;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mSongTitle.setText(mSongNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongNames.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView mSongTitle;

        public MyViewHolder(View itemView){
            super(itemView);
            mSongTitle = (TextView) itemView.findViewById(R.id.song_title);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickListener.clicked(position);

        }
    }
}
