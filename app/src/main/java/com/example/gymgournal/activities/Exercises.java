package com.example.gymgournal.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymgournal.adapter.ExerciseListAdapter;
import com.example.gymgournal.R;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.database.GgViewModel;

import java.util.List;

public class Exercises extends AppCompatActivity {

    public static final int ADD_EXERCISE_REQUEST_CODE = 1;

    private TextView mTextMessage;

    private GgViewModel mGgViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent HomeIntent = new Intent(getApplicationContext(), Home.class);
                    startActivity(HomeIntent);
                    finish();
                    return true;
                case R.id.navigation_exercise:
                    return true;
                case R.id.navigation_training:
                    Intent TrainingsIntent = new Intent(getApplicationContext(), Trainings.class);
                    startActivity(TrainingsIntent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        mGgViewModel = ViewModelProviders.of(this).get(GgViewModel.class);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_exercise);


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addExerciseIntent = new Intent(Exercises.this, AddExercise.class);
                startActivityForResult(addExerciseIntent, ADD_EXERCISE_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ExerciseListAdapter adapter =
                new ExerciseListAdapter(this, new ExerciseListAdapter.OnItemClickListener() {
            @Override public void onItemClick(Exercise exercise) {}
        });
        recyclerView.setAdapter(adapter);

        mGgViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable final List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXERCISE_REQUEST_CODE && resultCode == RESULT_OK) {
            Exercise exercise = new Exercise(data.getStringExtra(AddExercise.EXTRA_REPLY));
            mGgViewModel.insertExercise(exercise);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.exercise_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
