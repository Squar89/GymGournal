package com.example.gymgournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gymgournal.R;
import com.example.gymgournal.classes.Exercise;

import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseItemView;

        private ExerciseViewHolder(View itemView) {
            super(itemView);
            exerciseItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(final Exercise exercise, final OnItemClickListener listener) {
            exerciseItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(exercise);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Exercise> mExercise; //Cached copy of exercises
    private final OnItemClickListener listener;

    public ExerciseListAdapter(Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        if (mExercise != null) {
            Exercise current = mExercise.get(position);
            holder.exerciseItemView.setText(current.getExerciseName());
            holder.bind(mExercise.get(position), listener);
        }
        else {
            holder.exerciseItemView.setText("Data is not ready yet");
        }
    }

    public void setExercises(List<Exercise> exercises) {
        mExercise = exercises;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mExercise != null) {
            return mExercise.size();
        }
        else {
            return 0;
        }
    }
}
