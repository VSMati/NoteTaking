package com.example.notetaking.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;
import com.example.notetaking.recyclerview.ListAdapter;
import com.example.notetaking.recyclerview.ListClickListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListFragment extends Fragment {
    protected static final String EXTRA_POSITION = "positionNote";
    private NoteDatabase mDatabase;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        mDatabase = NoteDatabase.getInstance(requireActivity()
                .getApplicationContext());

        recyclerView = v.findViewById(R.id.recycler_view);
        listAdapter = new ListAdapter(getContext(), position -> {
            Intent intent = new Intent(getActivity(),NoteActivity.class);
            intent.putExtra(EXTRA_POSITION,position);
            intent.putExtra(MainActivity.EXTRA_NOTE_SHOWN,false);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        return v;
    }
    //TODO: after returning back to fragment it creates 2 notes
    /*TODO: after returning to fragment it doesn't show notes,
       but after pausing activity, they show up*/

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete){
            /*AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item
                    .getMenuInfo();*/
            int position = listAdapter.getPosition();
            deleteAndUpdate(position);
        }
        return super.onContextItemSelected(item);
    }

    private void deleteAndUpdate(int position){
        Note note = null;
        try {
            note = new InsertAnotherTask(ListFragment.this).execute().get().get(position);
            new InsertTask(ListFragment.this,note).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listAdapter.notifyDataSetChanged();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<ListFragment> activityReference;
        private Note note;

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
        private WeakReference<ListFragment> activityReference;

        public InsertAnotherTask(ListFragment context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return activityReference.get().mDatabase.getNoteDao().getAll();
        }
    }
}
