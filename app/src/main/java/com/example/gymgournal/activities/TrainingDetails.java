package com.example.gymgournal.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.gymgournal.R;
import com.example.gymgournal.adapter.ExerciseListAdapter;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.classes.Training;
import com.example.gymgournal.database.GgViewModel;

public class TrainingDetails extends AppCompatActivity {

    private Training training;
    private Long trainingId;
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
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);
        mGgViewModel = ViewModelProviders.of(this).get(GgViewModel.class);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* extract training id and find its Training object */
        Intent originIntent = getIntent();
        this.trainingId = originIntent.getLongExtra("trainingId", 0);

        new setupTrainingAsyncTask().execute();
    }

    private class setupTrainingAsyncTask extends AsyncTask<Void, Void, Void> {

        setupTrainingAsyncTask() {}

        @Override
        protected Void doInBackground(Void... params) {
            training = mGgViewModel.getTrainingFromId(trainingId);
            Log.i("SETUP_TRAINING", training.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.i("SETUP_TRAINING", "POST_EXECUTE");
            TextView mTrainingDescription = findViewById(R.id.training_desc);
            mTrainingDescription.setText(training.toString());

            RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            final ExerciseListAdapter adapter = new ExerciseListAdapter(getApplicationContext(),
                    new ExerciseListAdapter.OnItemClickListener() {
                @Override public void onItemClick(Exercise exercise) {}
            });
            recyclerView.setAdapter(adapter);
            adapter.setExercises(training.getExercises());

            RatingBar ratingBar = findViewById(R.id.ratingBarDetails);
            if (training.getRating() != 0) {
                ratingBar.setRating(training.getRating());
            }
            else {
                ratingBar.setVisibility(View.GONE);
            }
        }
    }
}
