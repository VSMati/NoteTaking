package com.example.notetaking.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteActivity extends AppCompatActivity {
    private EditText etTitle, etText;
    private Button btnSave;
    private boolean isNewNote;
    NoteDatabase mDatabase;
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
        mDatabase = NoteDatabase.getInstance(getApplicationContext());

        setAllValues();

        if (savedInstanceState != null){
            title[0] = savedInstanceState.getString(KEY_TITLE);
            text[0] = savedInstanceState.getString(KEY_TEXT);
        }

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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
            try {
                Note note = new RetrieveTask(NoteActivity.this).execute().get().get(position);
                etTitle.setText(note.getTitle());
                etText.setText(note.getContent());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private final WeakReference<NoteActivity> activityReference;
        private final Note note;

        InsertTask(NoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().mDatabase.getNoteDao().insert(note);
            return true;
        }
    }

    private static class UpdateTask extends AsyncTask<Void,Void,Boolean> {

        private final WeakReference<NoteActivity> activityReference;
        private final Note note;

        UpdateTask(NoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().mDatabase.getNoteDao().update(note);
            return true;
        }
    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>>{
        private final WeakReference<NoteActivity> activityReference;

        public RetrieveTask(NoteActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return activityReference.get().mDatabase.getNoteDao().getAll();
        }
    }
    private void saveNote(){
        if (etText.getText()!=null || etTitle.getText()!=null){
            String content = etText.getText().toString();
            String title = etTitle.getText().toString();
            if (!isNewNote){
                String id = getIntent().getStringExtra(EXTRA_UUID_STRING);
                Note note = Note.getNote(id,content,title);
                new UpdateTask(NoteActivity.this,note).execute();
            }else {
                Note note = new Note(content,title);
                new InsertTask(NoteActivity.this,note).execute();
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

