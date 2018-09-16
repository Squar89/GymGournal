package com.example.gymgournal.classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_table")
public class Exercise {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "exercise_name")
    private String exerciseName;

    public Exercise(@NonNull String exerciseName) {
        this.exerciseName = exerciseName;
    }

    @NonNull
    public String getExerciseName() {
        return this.exerciseName;
    }
}
