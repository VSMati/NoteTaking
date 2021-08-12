package com.example.notetaking.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Note {
    @PrimaryKey
    @NonNull
    private String mId;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "title")
    private String title;

    public Note(@NonNull String id, String content, String title) {
        this.mId=id;
        this.content = content;
        this.title = title;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
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
