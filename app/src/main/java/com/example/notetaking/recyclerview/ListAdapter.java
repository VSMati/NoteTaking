package com.example.notetaking.recyclerview;

import android.content.Context;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.Repository;
import com.example.notetaking.database.Note;
import com.example.notetaking.database.NoteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {
    //private static final String TAG = ListAdapter.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private static Repository sRepository;
    private final ListClickListener mListener;
    private int position;
    private List<Note> mList = new ArrayList<>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setList(List<Note> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public ListAdapter(Context context, ListClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        NoteDatabase database = NoteDatabase.getInstance(context);
        sRepository = Repository.getInstance(database);
        mListener = listener;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.list_cell,parent,false);
        return new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        final Note note = mList.get(position);
        holder.getTitle().setText(note.getTitle());
        holder.getText().setText(note.getContent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
            holder.itemView.setClipToOutline(true);
        }
        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
            ,View.OnClickListener {
        private final TextView mTitle;
        private final TextView mText;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_cell_title);
            mText = itemView.findViewById(R.id.item_cell_text);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getText() {
            return mText;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(Menu.NONE,R.id.delete, Menu.NONE,R.string.menu_delete);
            delete.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }

        private final MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = item -> {
            if (item.getItemId() == R.id.delete) {
                int position = ListAdapter.this.getPosition();
                deleteAndUpdate(position);
            }
            return true;
        };

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final Note retrievedNote = mList.get(position);
            String uuid = retrievedNote.getId();
            mListener.onClickItem(position,uuid);
            itemView.setOnCreateContextMenuListener(this);
        }
    }

    private void deleteAndUpdate(int position){
        final Note note = mList.get(position);

        sRepository.getDeleteCall(note).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        notifyDataSetChanged();
    }
}





