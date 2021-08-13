package com.example.notetaking.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private NoteDatabase mDatabase;
    private final ListClickListener mListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ListAdapter(Context context, ListClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        mDatabase = NoteDatabase.getInstance(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.list_cell,parent,false);
        return new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Note note = new RetrieveTask(this).execute().get().get(position);
            ((ListHolder)holder).getTitle().setText(note.getTitle());
            ((ListHolder)holder).getText().setText(note.getContent());
            holder.itemView.setOnLongClickListener(v -> {
                setPosition(holder.getAdapterPosition());
                return false;
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return new RetrieveTask(this).execute().get().size();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
            ,View.OnClickListener {
        private TextView mTitle;
        private TextView mText;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_cell_title);
            mText = itemView.findViewById(R.id.item_cell_text);
            itemView.setOnClickListener(this);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getText() {
            return mText;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE,R.id.delete, Menu.NONE,R.string.menu_delete);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            try {
                Note retrievedNote = new RetrieveTask(ListAdapter.this).execute()
                        .get().get(position);
                String uuid = retrievedNote.getId();
                mListener.onClickItem(position,uuid);
                itemView.setOnCreateContextMenuListener(this);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>> {

        private WeakReference<ListAdapter> activityReference;

        RetrieveTask(ListAdapter context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().mDatabase.getNoteDao().getAll();
            else
                return null;
        }
    }
}





