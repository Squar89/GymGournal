package com.example.gymgournal.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gymgournal.ExercisePair;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.classes.Training;
import com.example.gymgournal.dao.ExerciseDao;
import com.example.gymgournal.dao.TrainingDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GgRepository {

    private ExerciseDao mExerciseDao;
    private TrainingDao mTrainingDao;

    private LiveData<List<Exercise>> mExerciseList;
    private LiveData<List<Training>> mTrainingList;

    public GgRepository(Application application) {
        GgRoomDatabase db = GgRoomDatabase.getDatabase(application);
        mExerciseDao = db.exerciseDao();
        mExerciseList = mExerciseDao.getAllExercises();

        mTrainingDao = db.trainingDao();
        mTrainingList = mTrainingDao.getAllTrainings();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return mExerciseList;
    }

    public LiveData<List<Training>> getAllTrainings() {
        return mTrainingList;
    }

    public void insertExercise (Exercise exercise) {
        new insertExerciseAsyncTask(mExerciseDao).execute(exercise);
    }

    private static class insertExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {

        private ExerciseDao mAsyncTaskDao;

        insertExerciseAsyncTask(ExerciseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class constructTrainingParams {
        Date date;
        List<Exercise> exerciseList;
        int rating;

        constructTrainingParams(Date date, List<Exercise> exerciseList, int rating) {
            this.date = date;
            this.exerciseList = exerciseList;
            this.rating = rating;
        }
    }

    public void constructAndInsertTraining (Date date, List<Exercise> exerciseList, int rating) {
        constructTrainingParams params = new constructTrainingParams(date, exerciseList, rating);

        new constructAndInsertTrainingAsyncTask(mTrainingDao).execute(params);
    }

    private static class constructAndInsertTrainingAsyncTask extends
            AsyncTask<constructTrainingParams, Void, Void> {

        private TrainingDao mAsyncTaskDao;

        constructAndInsertTrainingAsyncTask(TrainingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final constructTrainingParams... params) {
            long nextId = mAsyncTaskDao.getLastId() + 1;

            Training training =
                    new Training(nextId, params[0].date, params[0].exerciseList, params[0].rating);
            mAsyncTaskDao.insertTraining(training);
            return null;
        }
    }

    private void wipeDatabase() {
        new wipeDatabaseAsyncTask(mExerciseDao, mTrainingDao).execute();
    }

    private static class wipeDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExerciseDao mExercisesDao;
        private TrainingDao mTrainingDao;

        wipeDatabaseAsyncTask(ExerciseDao ExercisesDao, TrainingDao TrainingDao) {
            mExercisesDao = ExercisesDao;
            mTrainingDao = TrainingDao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mExercisesDao.deleteAll();
            mTrainingDao.deleteAll();
            return null;
        }
    }

    public Training getTrainingFromId(long id) {
        return mTrainingDao.getTrainingFromId(id);
    }

    public Date getLastDate(Date date) {
        List<Training> trainingList = mTrainingDao.getListOfTrainings();

        for (Training t : trainingList) {
            if (!t.getDate().after(date)) {
                return t.getDate();
            }
        }

        return date;
    }

    public long getExercisesCount() {
        List<Training> trainingList = mTrainingDao.getListOfTrainings();
        long count = 0;

        for (Training t : trainingList) {
            count += t.getExercises().size();
        }

        return count;
    }

    public List<ExercisePair> getExerciseOccurrences() {
        List<Training> trainingList = mTrainingDao.getListOfTrainings();
        List<ExercisePair> exercisePairList = new ArrayList<>();

        boolean foundFlag;

        for (Training t : trainingList) {
            for (Exercise exercise : t.getExercises()) {
                foundFlag = false;
                for (ExercisePair exercisePair : exercisePairList) {
                    if (exercise.getExerciseName().equals(exercisePair.exercise.getExerciseName())) {
                        foundFlag = true;

                        exercisePair.occurrences += 1;
                    }
                }

                if (!foundFlag) {
                    exercisePairList.add(new ExercisePair(exercise, 1));
                }
            }
        }

        return exercisePairList;
    }

    /* Setup basic exercises and add few trainings (presentation purpose only) */
    public void setupExampleData() {
        wipeDatabase();

        Exercise ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10;

        ex1 = new Exercise("Bench Press");
        ex2 = new Exercise("Dips");
        ex3 = new Exercise("Push-Ups");
        ex4 = new Exercise("Pull-Ups");
        ex5 = new Exercise("Barbell Rows");
        ex6 = new Exercise("Overhead Press");
        ex7 = new Exercise("Squats");
        ex8 = new Exercise("Leg Press");
        ex9 = new Exercise("Deadlift");
        ex10 = new Exercise("Hammer Curls");

        insertExercise(ex1);
        insertExercise(ex2);
        insertExercise(ex3);
        insertExercise(ex4);
        insertExercise(ex5);
        insertExercise(ex6);
        insertExercise(ex7);
        insertExercise(ex8);
        insertExercise(ex9);
        insertExercise(ex10);

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date date1, date2, date3, date4, date5;
        try {
            date1 = df.parse("2018/09/01");
            date2 = df.parse("2018/09/02");
            date3 = df.parse("2018/09/03");
            date4 = df.parse("2018/09/04");
            date5 = df.parse("2018/09/05");

            constructAndInsertTraining(date1, Arrays.asList(ex1, ex2, ex9), 2);
            constructAndInsertTraining(date2, Arrays.asList(ex3, ex8, ex4, ex6, ex9, ex10), 3);
            constructAndInsertTraining(date3, Arrays.asList(ex2, ex8, ex9, ex10), 1);
            constructAndInsertTraining(date4, Arrays.asList(ex1, ex7, ex9, ex3, ex5), 0);
            constructAndInsertTraining(date5, Arrays.asList(ex7, ex9, ex6, ex4), 2);
        }
        catch(ParseException e) {
            Log.i("SETUP_EXAMPLES", "Check hardcoded strings");
        }
    }
}
