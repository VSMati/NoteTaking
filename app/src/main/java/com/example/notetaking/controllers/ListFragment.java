package com.example.notetaking.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.recyclerview.ListAdapter;

public class ListFragment extends Fragment {
    protected static final String EXTRA_POSITION = "positionNote";
    protected static RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);

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
}
