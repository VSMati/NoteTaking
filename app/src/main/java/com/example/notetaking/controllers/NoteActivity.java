package com.example.notetaking.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notetaking.R;
import com.example.notetaking.Repository;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteActivity extends AppCompatActivity {
    private static final String TAG = NoteActivity.class.getSimpleName();
    private EditText etTitle, etText;
    private Button btnSave;
    private boolean isNewNote;
    private static Repository sRepository;
    private final String[] title = new String[1];
    private final String[] text = new String[1];

    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String EXTRA_UUID_STRING = "uuid_string";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);
        btnSave = findViewById(R.id.btnSave);
        NoteDatabase database = NoteDatabase.getInstance(getApplicationContext());
        sRepository = Repository.getInstance(database);

        setAllValues();

        if (savedInstanceState != null){
            title[0] = savedInstanceState.getString(KEY_TITLE);
            text[0] = savedInstanceState.getString(KEY_TEXT);
        }

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text[0] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                text[0] = s.toString();
            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title[0] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                title[0] = s.toString();
            }
        });

        btnSave.setOnClickListener(v -> saveNote());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE,title[0]);
        outState.putString(KEY_TEXT,text[0]);
    }

    public void setAllValues(){
        isNewNote = getIntent().getBooleanExtra(MainActivity.EXTRA_NOTE_SHOWN,false);
        if (!isNewNote){
            int position = getIntent().getIntExtra(ListFragment.EXTRA_POSITION,0);
                final Note[] note = new Note[1];
                sRepository.getRetrieveCall()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Note>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) { }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Note> notes) {
                                note[0] = notes.get(position);
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable t) {
                                Log.e(TAG, "onError: ", t.fillInStackTrace());
                            }

                            @Override
                            public void onComplete() {
                                etTitle.setText(note[0].getTitle());
                                etText.setText(note[0].getContent());
                            }
                        });
        }
    }

    private void saveNote(){
        if (etText.getText()!=null || etTitle.getText()!=null){
            String content = etText.getText().toString();
            String title = etTitle.getText().toString();
            if (!isNewNote){
                String id = getIntent().getStringExtra(EXTRA_UUID_STRING);
                Note note = Note.getNote(id,content,title);
                sRepository.getUpdateCall(note)
                .subscribeOn(Schedulers.newThread()).subscribe(); //update old one
            }else {
                Note note = new Note(content,title);
                sRepository.getInsertCall(note)
                .subscribeOn(Schedulers.newThread()).subscribe(); //insert new one
            }
        }
        assert ListFragment.recyclerView.getAdapter() != null;
        ListFragment.recyclerView.getAdapter().notifyDataSetChanged();
        finish();

    }

    public static Intent newIntent(Context startingAct,String uuid){
        Intent intent = new Intent(startingAct,NoteActivity.class);
        intent.putExtra(EXTRA_UUID_STRING,uuid);
        return intent;
    }
}

