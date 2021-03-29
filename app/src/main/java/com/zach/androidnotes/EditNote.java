package com.zach.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

public class EditNote extends AppCompatActivity {
    private Note note;
    private EditText content;
    private EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#3B70C4"));
        content = findViewById(R.id.contentEditText);
        title = findViewById(R.id.titleEditText);

        Intent intent = getIntent();
        if (intent.hasExtra("Note")) {
            note = (Note) intent.getSerializableExtra("Note");
            if (note != null) {
                title.setText(note.getTitle());
                content.setText(note.getContent());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // note to self: inflater converts xml layout to live object
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //only make note if title
        if (item.getItemId() == R.id.save_note) {
            if (!title.getText().toString().trim().isEmpty()) {
                doReturn();
            } else emptyNoteDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void emptyNoteDialog() { // I did a dialog box instead of a toast for practice
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter a title before saving.");
        builder.setNegativeButton("Cancel Note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title.getText().toString().trim().isEmpty()) {
            builder.setTitle("The note will be not be saved without a title");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        } else {
            builder.setTitle("Your note is not saved! Save note '" + title.getText().toString() + "'?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                        doReturn();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            // super.onBackPressed();
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void doReturn() {
        String contentStr = content.getText().toString();
        String titleStr = title.getText().toString();
        if (note != null) {
            if (!note.getContent().equals(contentStr) || !note.getTitle().equals(titleStr)) {
                note.setNewDate(new Date());
                note.setContent(contentStr);
                note.setTitle(titleStr);
            } else finish();
        } else {
            note = new Note(titleStr, contentStr);
            note.setNewDate(new Date());
        }
        Intent intent = new Intent();
        intent.putExtra("Note", note);
        setResult(RESULT_OK, intent);
        finish();

    }
}
