package com.example.gymgournal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymgournal.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddTraining extends AppCompatActivity {

    public static final int ADD_EXERCISE_TO_TRAINING_REQUEST_CODE = 3;

    private EditText mEditDateView;
    private TextView mTextExercisesCount;

    @SuppressLint("SimpleDateFormat") final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private int rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);
        mEditDateView = findViewById(R.id.edit_date);
        mTextExercisesCount = findViewById(R.id.exercises_text);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Date currentDate = new Date();
        mEditDateView.setText(dateFormat.format(currentDate));

        setupRatingBar();

        final Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();

                if (validateFields()) {
                    String dateString = mEditDateView.getText().toString();
                    replyIntent.putExtra("dateTraining", dateString);
                    Log.i("DATE", dateString);

                    replyIntent.putExtra("ratingTraining", getRating());
                    Log.i("RATING", Float.toString(rating));

                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });

        final Button addExercisesButton = findViewById(R.id.add_exercises_to_training_button);
        addExercisesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent addExercisesIntent = new Intent(getApplicationContext(), ExercisesToAdd.class);
                startActivityForResult(addExercisesIntent, ADD_EXERCISE_TO_TRAINING_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXERCISE_TO_TRAINING_REQUEST_CODE && resultCode == RESULT_OK) {
            String textToDisplay = "Current exercises count: " + Trainings.getExerciseListToAddSize();
            mTextExercisesCount.setText(textToDisplay);
        } else {
            Toast.makeText(
                    getApplicationContext(), R.string.exercise_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    boolean validateFields() {
        //reset errors
        mEditDateView.setError(null);

        if (TextUtils.isEmpty(mEditDateView.getText())) {
            mEditDateView.setError("This field cannot be empty");
            return false;
        }

        if (mEditDateView.getText().length() > 10) {
            mEditDateView.setError("Wrong date format. Example: 2020/01/21");
            return false;
        }

        try {
            dateFormat.parse(mEditDateView.getText().toString());
        }
        catch (ParseException e) {
            mEditDateView.setError("Wrong date format. Example: 2020/01/21");
            return false;
        }

        return true;
    }

    public void setupRatingBar() {
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRating(Math.round(rating));
            }
        });
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
