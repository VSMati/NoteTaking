package com.example.notetaking.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NoteDao {
    /**
     * @return Observable might emit every time new value is present */
    @Query("SELECT * FROM note")
    Observable<List<Note>> getAll();
    /**@return Single just returns one response*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<Long> insert(Note note);
    /**@return Completable indicates completion or error*/
    @Delete
    Completable delete(Note note);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    Completable update(Note note);

    @Transaction
    default void upsert(Note entity) {
        long id = insert(entity).blockingGet();
        if (id == -1) {
            update(entity);
        }
    }

}
