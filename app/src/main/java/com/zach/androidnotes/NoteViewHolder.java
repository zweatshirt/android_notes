package com.zach.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView date;
    TextView content;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.titleText);
        date = itemView.findViewById(R.id.dateText);
        content = itemView.findViewById(R.id.contentText);
    }
}
