package com.example.notetaking.database;

import android.content.Context;
import android.provider.SyncStateContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase noteDb;

    public abstract NoteDao getNoteDao();

    public static NoteDatabase getInstance(Context context) {
        if (noteDb == null){
            noteDb = buildDatabaseInstance(context);
        }
        return noteDb;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NoteDatabase.class,
                "database.db")
                .build();
    }

}
