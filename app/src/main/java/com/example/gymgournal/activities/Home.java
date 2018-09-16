package com.example.gymgournal.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymgournal.ExercisePair;
import com.example.gymgournal.R;
import com.example.gymgournal.database.GgViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView mTextStat1;
    private TextView mTextStat2;

    private GgViewModel mGgViewModel;
    private List<ExercisePair> mExercisesOccurrences;

    PieChart pieChart;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_exercise:
                    Intent ExercisesIntent = new Intent(getApplicationContext(), Exercises.class);
                    startActivity(ExercisesIntent);
                    return true;
                case R.id.navigation_training:
                    Intent TrainingsIntent = new Intent(getApplicationContext(), Trainings.class);
                    startActivity(TrainingsIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGgViewModel = ViewModelProviders.of(this).get(GgViewModel.class);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        mTextStat1 = findViewById(R.id.stat1);
        new setupStat1AsyncTask().execute();

        mTextStat2 = findViewById(R.id.stat2);
        new setupStat2AsyncTask().execute();

        /*========================================================================================*/
        /* THIS SHOULD BE COMMENTED AFTER SUCCESSFUL FIRST RUN */
        /* this function setups example data only for presentation purposes */
        /* if not commented, every visit at home activity will wipe database and setup the same */
        /* example objects */
        mGgViewModel.setupExampleData();//TODO
        /*========================================================================================*/

        new setupChartAsyncTask().execute();
    }

    private void setupChart() {
        pieChart = findViewById(R.id.exercises_chart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(20);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.getLegend().setEnabled(false);

        loadChartData();

        addListener();
    }

    private void loadChartData() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < mExercisesOccurrences.size() && i < 10; i++) {
            Log.i("OCCURRENCES_LIST", mExercisesOccurrences.get(i).exercise.getExerciseName()
                    + " " + mExercisesOccurrences.get(i).occurrences);
            yEntrys.add(new PieEntry(mExercisesOccurrences.get(i).occurrences, i));
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Your top exercises");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(0);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        colors.add(Color.RED);
        colors.add(Color.LTGRAY);

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void addListener() {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i("ENTRY_TO_STRING", e.toString());
                Log.i("HIGHLIGHT_TO_STRING", h.toString());
                int pos1 = h.toString().indexOf("x: ");
                int pos2 = h.toString().indexOf(".");
                String xValueString = h.toString().substring(pos1 + 3, pos2);
                int xValue = Integer.parseInt(xValueString);
                Log.i("X_VALUE", xValueString);

                String exercise = mExercisesOccurrences.get(xValue).exercise.getExerciseName();
                long yValue = mExercisesOccurrences.get(xValue).occurrences;
                Log.i("Y_VALUE", "" + yValue);

                Toast.makeText(Home.this, "You trained " + exercise +
                        " for a total of " + yValue + " times!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {}
        });
    }

    private class setupStat1AsyncTask extends AsyncTask<Void, Void, Void> {

        private long daysSinceLastTraining;

        setupStat1AsyncTask() {}

        @Override
        protected Void doInBackground(Void... params) {
            Date todayDate = new Date();
            Date lastDate = mGgViewModel.getLastDate(todayDate);

            long diffInMillies = Math.abs(todayDate.getTime() - lastDate.getTime());
            daysSinceLastTraining = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            String stat1 = "Days since last training: " + daysSinceLastTraining;
            mTextStat1.setText(stat1);
        }
    }

    private class setupStat2AsyncTask extends AsyncTask<Void, Void, Void> {

        private long exercisesCount;

        setupStat2AsyncTask() {}

        @Override
        protected Void doInBackground(Void... params) {
            exercisesCount = mGgViewModel.getExercisesCount();

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            String stat2 = "Number of exercises completed: " + exercisesCount;
            mTextStat2.setText(stat2);
        }
    }

    private class setupChartAsyncTask extends AsyncTask<Void, Void, Void> {

        setupChartAsyncTask() {}

        @Override
        protected Void doInBackground(Void... params) {
            mExercisesOccurrences = mGgViewModel.getExerciseOccurrences();

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            setupChart();
        }
    }
}
