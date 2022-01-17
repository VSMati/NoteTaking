package com.example.notetaking;

import android.content.Context;

import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
/**
 * This class provides datasource in form of methods, which are used in RxJava streams
 * */
public class Repository {
    private static NoteDatabase sNoteDatabase;

    private Repository(NoteDatabase noteDatabase) {
        if (sNoteDatabase == null) {
            sNoteDatabase = noteDatabase;
        }
    }
    /**Get instance with database*/
    public static Repository getInstance(NoteDatabase database) {
        return new Repository(database);
    }
    /**Get instance by context*/
    public static Repository getInstance(Context context) {
        return new Repository(NoteDatabase.getInstance(context));
    }

    public Observable<List<Note>> getRetrieveCall() {
        return sNoteDatabase.getNoteDao().getAll();
    }

    public Completable getUpdateCall(Note note) {
        return sNoteDatabase.getNoteDao().update(note);
    }

    public Completable getDeleteCall(Note note) {
        return sNoteDatabase.getNoteDao().delete(note);
    }

    public Single<Long> getInsertCall(Note note) {
        return sNoteDatabase.getNoteDao().insert(note);
    }
}
