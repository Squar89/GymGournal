package com.example.gymgournal.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.gymgournal.classes.Exercise;
import com.example.gymgournal.classes.Training;
import com.example.gymgournal.dao.ExerciseDao;
import com.example.gymgournal.dao.TrainingDao;

@Database(entities = {Exercise.class, Training.class}, version = 2)
public abstract class GgRoomDatabase extends RoomDatabase {

    public abstract ExerciseDao exerciseDao();

    public abstract TrainingDao trainingDao();

    private static GgRoomDatabase INSTANCE;

    public static GgRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GgRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GgRoomDatabase.class, "gg_database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }

        return INSTANCE;
    }
}
