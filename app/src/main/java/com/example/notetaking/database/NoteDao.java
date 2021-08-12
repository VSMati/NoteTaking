package com.example.notetaking.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Note note);

    @Delete
    void delete(Note note);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Note note);

    @Transaction
    default void upsert(Note entity) {
        long id = insert(entity);
        if (id == -1) {
            update(entity);
        }
    }

}
