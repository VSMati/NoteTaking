package com.example.notetaking.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.util.UUID;

public class NoteFragment extends Fragment {
    private EditText etTitle, etText;
    private boolean isNewNote;
    private NoteDatabase mDatabase = NoteDatabase.getInstance(getContext());
    private final String[] title = new String[1];
    private final String[] text = new String[1];

    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note,container);
        etTitle = v.findViewById(R.id.etTitle);
        etText = v.findViewById(R.id.etText);

        if (savedInstanceState != null){
            title[0] = savedInstanceState.getString(KEY_TITLE);
            text[0] = savedInstanceState.getString(KEY_TEXT);
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    @Override
    public void onPause() {
        super.onPause();
        Note note = new Note(Integer.getInteger(UUID.randomUUID().toString()),text[0],title[0]);
        mDatabase.getNoteDao().update(note);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE,title[0]);
        outState.putString(KEY_TEXT,text[0]);
    }
}
