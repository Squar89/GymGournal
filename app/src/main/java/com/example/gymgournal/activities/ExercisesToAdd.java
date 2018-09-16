package com.example.gymgournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gymgournal.R;
import com.example.gymgournal.adapter.ExerciseListAdapter;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.database.GgViewModel;

import java.util.List;

public class ExercisesToAdd extends AppCompatActivity {

    private GgViewModel mGgViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_to_add);
        mGgViewModel = ViewModelProviders.of(this).get(GgViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ExerciseListAdapter adapter = new ExerciseListAdapter(this, new ExerciseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
                Intent newIntent = new Intent();
                Trainings.addToExerciseList(exercise);
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);

        mGgViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable final List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });
    }
}
