package com.example.notetaking.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;
import com.example.notetaking.recyclerview.ListAdapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListFragment extends Fragment {
    protected static final String EXTRA_POSITION = "positionNote";
    private NoteDatabase mDatabase;
    protected static RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        mDatabase = NoteDatabase.getInstance(requireActivity()
                .getApplicationContext());

        boolean isLand = requireContext().getResources().getBoolean(R.bool.landscape);
        boolean isTablet = requireContext().getResources().getBoolean(R.bool.isTablet);

        recyclerView = v.findViewById(R.id.recycler_view);
        listAdapter = new ListAdapter(getContext(), (position, uuid) -> {
            Intent intent = NoteActivity.newIntent(getContext(),uuid);
            intent.putExtra(EXTRA_POSITION,position);
            intent.putExtra(MainActivity.EXTRA_NOTE_SHOWN,false);
            startActivity(intent);
        });

        if (isLand || isTablet){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete){
            int position = listAdapter.getPosition();
            deleteAndUpdate(position);
        }
        return super.onContextItemSelected(item);
    }

    private void deleteAndUpdate(int position){
        Note note;
        try {
            note = new InsertAnotherTask(ListFragment.this).execute().get().get(position);
            new InsertTask(ListFragment.this,note).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listAdapter.notifyDataSetChanged();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private final WeakReference<ListFragment> activityReference;
        private final Note note;

        InsertTask(ListFragment context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        protected Boolean doInBackground(Void... objs) {
            activityReference.get().mDatabase.getNoteDao().delete(note);
            return true;
        }
    }

    private static class InsertAnotherTask extends AsyncTask<Void,Void, List<Note>>{
        private final WeakReference<ListFragment> activityReference;

        public InsertAnotherTask(ListFragment context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return activityReference.get().mDatabase.getNoteDao().getAll();
        }
    }
}
