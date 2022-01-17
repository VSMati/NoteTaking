package com.example.notetaking.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaking.R;
import com.example.notetaking.Repository;
import com.example.notetaking.database.Note;
import com.example.notetaking.recyclerview.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    protected static final String EXTRA_POSITION = "positionNote";
    private static Repository sRepository;
    protected static RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Note> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        sRepository = Repository.getInstance(requireContext());

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
        updateAdapterList();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapterList();
    }
    /**Pass a new value of list to LIstAdapter every time Observable emits
     * Notifies ListAdapter about this*/
    private void updateAdapterList() {
        sRepository.getRetrieveCall().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) { }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Note> notes) {
                        mList = notes;
                        listAdapter.setList(mList);
                        listAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: can't pass list to ListAdapter",e.fillInStackTrace());
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
