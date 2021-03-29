package com.zach.androidnotes;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> noteList;
    private final MainActivity mainActivity;

    public NoteListAdapter (List<Note> noteList, MainActivity mainActivity) {
        this.noteList = noteList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_entry, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        if (note.getContent().length() > 80) {
            String concatContent = note.getContent().substring(0, 80);
            concatContent += "...";
            holder.content.setText(concatContent);
        }
        else holder.content.setText(note.getContent());
        holder.date.setText(note.getDateString());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
