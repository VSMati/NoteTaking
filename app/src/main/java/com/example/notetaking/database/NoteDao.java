package com.example.notetaking.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);
}
