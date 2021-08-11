package com.example.notetaking.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

public class ListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private NoteDatabase mDatabase;
    private final ListClickListener mListener;

    public ListAdapter(Context context, ListClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        mDatabase = NoteDatabase.getInstance(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.list_cell,parent);
        return new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note note = mDatabase.getNoteDao().getAll().get(position);
    }

    @Override
    public int getItemCount() {
        return mDatabase.getNoteDao().getAll().size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_cell);
            itemView.setOnClickListener(v -> mListener.onClickItem(getAdapterPosition()));

        }
    }
}


