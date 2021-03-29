package com.zach.androidnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/* Zachery Linscott | CS442 Lab 2
* Notes Application.
* Note to self:
* In Main Activity, the position of the view holder object only changes if text is updated,
* even if the user hits save (though I would've preferred a 'Done' btn, similar to Apple's Notes).
* Upon successful save, the position of the view holder changes to the top, and the time stamp
* is updated.
*
* Things to remember from this project:
* JSON file transferring upon onStop, onDestroy, and onCreate. I don't fully understand the
* implications of saving on onStop and whether it can cause issues but I think it was the
* correct thing to do. Professor Hield's way of transferring JSON is pretty cool.
*
* I want to refactor my code if I have time. It's kinda a mess in some places.
* I ellipsized programmatically but could've done the same thing easily in the XML
* and get rid of what feels like a bad practice. Also I should've posted notes to the top
* by sorting the list by date, instead I removed the item and set it at position 0.
 */
/* todo:
* implement JSON file to save data and populate recycler on start up/ remove from JSON
* on delete (maybe done)
*
* if file doesn't exist, create new one
*
* fix/debug StringBuilder crash on startup???
*
*
* fix text going outside text view
*
* make recycler view viewholders look nicer on screen
* */

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private List<Note> noteList = new ArrayList<>();
    private RecyclerView rView;
    private static final int ONCLICK_CODE = 1;
    private static final int NEW_NOTE_CODE = 2;
    private Note note;
    private NoteListAdapter adapter;
    private int pos = -1;
=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#3B70C4"));

        rView = findViewById(R.id.recylerView);
        // create adapter and set to recycler view
        adapter = new NoteListAdapter(noteList, this);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(this)); // define rView layout
        noteList.clear();

        noteList.addAll(loadFile()); //implement loadFile
        adapter.notifyDataSetChanged();
        if (noteList != null) {
            getSupportActionBar().setTitle("Android Notes" + " (" + noteList.size() + ")");
        }
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x033280));
    }

    private List<Note> loadFile() { // add file name param
        List<Note> fileNoteList = new ArrayList<>();
        String fileName = getString(R.string.file_name); // change to param
        try {
            InputStream iStream = getApplication().getApplicationContext().openFileInput(fileName);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(iStream, StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                builder.append(line);

            JSONArray jsonArray = new JSONArray(builder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String title = jsonObj.getString("title");
                String content = jsonObj.getString("content");
                String date = jsonObj.getString("date");
                Note note = new Note(title, content, date);
                noteList.add(note);
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "JSON File " + fileName + " not present.", Toast.LENGTH_SHORT).show();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return fileNoteList;
    }

    private void saveData() {
        try {
            FileOutputStream outStream = getApplicationContext()
                    .openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(outStream);
            printWriter.print(noteList);
            printWriter.close();
            outStream.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    //    @Override
//    protected void onPause() {
//        saveData();
//        super.onPause();
//    }

//    @Override
//    protected void onResume() {
//        noteList.clear();
//        noteList.addAll(loadFile()); //implement loadFile
//        adapter.notifyDataSetChanged();
//        super.onResume();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // note to self: inflater converts xml layout to live object
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // menu options selection
        if (item.getItemId() == R.id.add_note) { //switch to note activity
            Intent addNote = new Intent(this, EditNote.class);
            startActivityForResult(addNote, NEW_NOTE_CODE);
        }
        if (item.getItemId() == R.id.info) { // switch to info activity
            Intent appInfoSwitch = new Intent(this, AppInfo.class);
            startActivity(appInfoSwitch);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {  // on click for notes to go to edit activity
        pos = rView.getChildLayoutPosition(v);
        Note note = noteList.get(pos);
        Intent intent = new Intent(this, EditNote.class);
        intent.putExtra("Note", note);

        startActivityForResult(intent, ONCLICK_CODE);
    }

    // TODO: Finish
    @Override
    public boolean onLongClick(View v) {  // long click listener, delete if long click
        int pos = rView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note '" + noteList.get(pos).getTitle() + "'?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(pos);
                adapter.notifyDataSetChanged();
                if (noteList != null) { //just in case yanno
                    getSupportActionBar().setTitle("Android Notes" + " (" + noteList.size() + ")");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    // pulls data from intent along w/ result on return from EditNote
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            note = (Note) data.getSerializableExtra("Note");
        }
        if (requestCode == ONCLICK_CODE) {
            if (resultCode == RESULT_OK) {
                if (note != null && pos > -1) {
                    noteList.remove(pos);
                    noteList.add(0, note);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        else if (requestCode == NEW_NOTE_CODE) {
            if (resultCode == RESULT_OK) {
                if (note != null) {
                    noteList.add(0, note);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (noteList != null) { //just in case yanno
            getSupportActionBar().setTitle("Android Notes" + " (" + noteList.size() + ")");
        }

    }
}