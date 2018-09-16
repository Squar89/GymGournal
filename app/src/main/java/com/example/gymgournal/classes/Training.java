package com.example.gymgournal.classes;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(tableName = "training_table")
@TypeConverters(TrainingConverter.class)
public class Training {

    @PrimaryKey
    private long id;

    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

    @NonNull
    private List<Exercise> exercises;

    private int rating;

    public Training(long id, @NonNull Date date, @NonNull List<Exercise> exercises, int rating) {
        synchronized (Training.class) {
            this.id = id;
            this.date = date;
            this.exercises = exercises;
            this.rating = rating;
        }

        Log.i("TRAINING_ID", Long.toString(this.id));
    }

    public long getId() {
        return this.id;
    }

    @NonNull
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NonNull
    public List<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        return "Training " + Long.toString(getId()) + " - " + df.format(getDate());
    }
}