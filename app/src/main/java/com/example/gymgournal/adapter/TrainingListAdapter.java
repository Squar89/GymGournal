package com.example.gymgournal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gymgournal.R;
import com.example.gymgournal.classes.Training;

import java.util.List;

public class TrainingListAdapter extends RecyclerView.Adapter<TrainingListAdapter.TrainingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Training training);
    }

    class TrainingViewHolder extends RecyclerView.ViewHolder {
        private final TextView trainingItemView;

        private TrainingViewHolder(View itemView) {
            super(itemView);
            trainingItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(final Training training, final OnItemClickListener listener) {
            trainingItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(training);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Training> mTraining; //Cached copy of trainings
    private final OnItemClickListener listener;

    public TrainingListAdapter(Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new TrainingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrainingViewHolder holder, int position) {
        if (mTraining != null) {
            Training current = mTraining.get(position);
            holder.trainingItemView.setText(current.toString());
            holder.bind(mTraining.get(position), listener);
        }
        else {
            holder.trainingItemView.setText("Data is not ready yet");
        }
    }

    public void setTrainings(List<Training> trainings) {
        mTraining = trainings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTraining != null) {
            return mTraining.size();
        }
        else {
            return 0;
        }
    }
}
