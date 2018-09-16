package com.example.gymgournal.classes;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class TrainingConverter {
    @TypeConverter
    public static List<Exercise> stringToExercises(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Exercise>>() {}.getType();
        List<Exercise> exercises = gson.fromJson(json, type);
        return exercises;
    }

    @TypeConverter
    public static String exercisesToString(List<Exercise> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Exercise>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public static Date stringToDate(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Date>() {}.getType();
        Date date = gson.fromJson(json, type);
        return date;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        Gson gson = new Gson();
        Type type = new TypeToken<Date>() {}.getType();
        String json = gson.toJson(date, type);
        return json;
    }
}
