package com.example.gymgournal;

import com.example.gymgournal.classes.Exercise;

public class ExercisePair {
    public Exercise exercise;
    public long occurrences;

    public ExercisePair(Exercise exercise, long occurrences) {
        this.exercise = exercise;
        this.occurrences = occurrences;
    }
}
