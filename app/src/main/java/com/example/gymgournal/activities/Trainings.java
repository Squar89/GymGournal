package com.example.gymgournal.activities;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymgournal.R;
import com.example.gymgournal.adapter.TrainingListAdapter;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.classes.Training;
import com.example.gymgournal.database.GgViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trainings extends AppCompatActivity {

    public static final int ADD_TRAINING_REQUEST_CODE = 2;

    private TextView mTextMessage;

    private static List<Exercise> exerciseListToAdd;

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
                    Intent ExercisesIntent = new Intent(getApplicationContext(), Exercises.class);
                    startActivity(ExercisesIntent);
                    finish();
                    return true;
                case R.id.navigation_training:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings);
        mGgViewModel = ViewModelProviders.of(this).get(GgViewModel.class);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_training);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTrainingIntent = new Intent(Trainings.this, AddTraining.class);
                exerciseListToAdd = new ArrayList<>();
                startActivityForResult(addTrainingIntent, ADD_TRAINING_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final TrainingListAdapter adapter =
                new TrainingListAdapter(this, new TrainingListAdapter.OnItemClickListener() {
                    @Override public void onItemClick(Training training) {
                        Log.i("TRAINING LISTENER", training.toString());

                        Intent trainingDetailsIntent = new Intent(Trainings.this, TrainingDetails.class);
                        trainingDetailsIntent.putExtra("trainingId", training.getId());
                        startActivity(trainingDetailsIntent);
                    }
        });
        recyclerView.setAdapter(adapter);

        mGgViewModel.getAllTrainings().observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(@Nullable final List<Training> trainings) {
                adapter.setTrainings(trainings);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TRAINING_REQUEST_CODE && resultCode == RESULT_OK) {
            @SuppressLint("SimpleDateFormat") final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date dateTraining = dateFormat.parse(data.getStringExtra("dateTraining"));
                int ratingTraining = data.getIntExtra("ratingTraining", 0);

                mGgViewModel.insertTraining(dateTraining, exerciseListToAdd, ratingTraining);
            } catch (ParseException e) {/* this string was validated earlier */}
        } else {
            Toast.makeText(
                    getApplicationContext(), R.string.training_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public static void addToExerciseList(Exercise exercise) {
        exerciseListToAdd.add(exercise);
    }

    public static int getExerciseListToAddSize() {
        return exerciseListToAdd.size();
    }
}
