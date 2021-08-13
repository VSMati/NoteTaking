package com.example.notetaking.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity
public class Note {
    @PrimaryKey
    @NonNull
    private String mId;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "title")
    private String title;

    public Note(String content, String title) {
        this.mId= UUID.randomUUID().toString();
        this.content = content;
        this.title = title;
    }

    private Note(String id, String content, String title){
        this.mId = id;
        this.content = content;
        this.title = title;
    }

    public static Note getNote(String id, String content, String title){
        return new Note(id,content,title);
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
