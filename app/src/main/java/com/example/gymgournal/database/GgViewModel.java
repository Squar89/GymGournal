package com.example.gymgournal.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.gymgournal.ExercisePair;
import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.classes.Training;

import java.util.Date;
import java.util.List;

public class GgViewModel extends AndroidViewModel {

    private GgRepository mRepository;

    private LiveData<List<Exercise>> mAllExercises;
    private LiveData<List<Training>> mAllTrainings;

    public GgViewModel(Application application) {
        super(application);

        mRepository = new GgRepository(application);
        mAllExercises = mRepository.getAllExercises();
        mAllTrainings = mRepository.getAllTrainings();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return mAllExercises;
    }

    public LiveData<List<Training>> getAllTrainings() {
        return mAllTrainings;
    }

    public void insertExercise(Exercise exercise) {
        mRepository.insertExercise(exercise);
    }

    public void insertTraining(Date date, List<Exercise> exerciseList, int rating) {
        mRepository.constructAndInsertTraining(date, exerciseList, rating);
    }

    public Training getTrainingFromId(long id) {
        return mRepository.getTrainingFromId(id);
    }

    public Date getLastDate(Date date) {
        return mRepository.getLastDate(date);
    }

    public long getExercisesCount() {
        return mRepository.getExercisesCount();
    }

    public List<ExercisePair> getExerciseOccurrences() {
        return mRepository.getExerciseOccurrences();
    }

    public void setupExampleData() {
        mRepository.setupExampleData();
    }
}
