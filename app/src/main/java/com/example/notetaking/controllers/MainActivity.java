package com.example.notetaking.controllers;

import android.content.Intent;
import android.os.Bundle;

import com.example.notetaking.R;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notetaking.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected static final String EXTRA_NOTE_SHOWN = "isNoteShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.notetaking.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        FragmentManager fm = getSupportFragmentManager();
        ListFragment lf = new ListFragment();
        fm.beginTransaction().add(R.id.list_container,lf).commit();

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,NoteActivity.class);
            intent.putExtra(EXTRA_NOTE_SHOWN, true);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}