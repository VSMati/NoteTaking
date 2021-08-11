package com.example.notetaking.controllers;

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
import com.example.notetaking.recyclerview.ListClickListener;

public class ListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        ListAdapter listAdapter = new ListAdapter(getContext(), new ListClickListener() {
            @Override
            public void onClickItem(int position) {

            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(listAdapter);
        return v;
    }
}
