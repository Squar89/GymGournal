package com.example.gymgournal.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.gymgournal.classes.Training;

import java.util.Date;
import java.util.List;

@Dao
public interface TrainingDao {

    @Insert
    void insertTraining(Training training);

    @Delete
    void deleteTraining(Training training);

    @Query("DELETE FROM training_table")
    void deleteAll();

    @Query("SELECT * FROM training_table ORDER BY id DESC")
    LiveData<List<Training>> getAllTrainings();

    @Query("SELECT * FROM training_table ORDER BY id DESC")
    List<Training> getListOfTrainings();

    @Query("SELECT MAX(training_table.id) FROM training_table")
    long getLastId();

    @Query("SELECT * FROM training_table WHERE training_table.id = :id")
    Training getTrainingFromId(long id);
}
