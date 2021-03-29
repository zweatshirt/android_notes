package com.zach.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    private String title;
    private String content;
    private String dateString;
    private final String pattern = "EEE, MMM d yyyy 'at' HH:mmaa";
    public static int noteNum;

    public Note (String title, String content) {
        this.title = title;
        this.content = content;
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
        dateString = df.format(new Date());

        noteNum++;
    }

    public Note (String title, String content, String dateString) {
        this.title = title;
        this.content = content;
        this.dateString = dateString;

        noteNum++;
    }



    public String getTitle() { return title; }

    public String getContent() {return content; }

    public String getDateString() {return dateString; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNewDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
        dateString = df.format(new Date());

    }

    @NonNull
    @Override
    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("content").value(getContent());
            jsonWriter.name("date").value(dateString);
            jsonWriter.endObject();
            jsonWriter.close();
            return stringWriter.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
