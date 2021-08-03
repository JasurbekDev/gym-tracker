package com.idyllic.activitytracker.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.data.models.Activity;

public class HistoryAdapter extends ListAdapter<Activity, HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private HistoryAdapterListener historyAdapterListener;

    public HistoryAdapter(Context context, HistoryAdapterListener historyAdapterListener) {
        // DiffUtil class implementation. Source: https://hackathon-blog-42.medium.com/listadapter-renewed-9b5b496198e2
        super(Activity.DIFF_CALLBACK);
        this.context = context;
        this.historyAdapterListener = historyAdapterListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view, context, historyAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Activity currentActivity = getItem(position);
        holder.bind(currentActivity);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private Context context;
        private HistoryAdapterListener historyAdapterListener;

        public HistoryViewHolder(@NonNull View itemView, Context context, HistoryAdapterListener historyAdapterListener) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            this.historyAdapterListener = historyAdapterListener;
        }

        public void bind(Activity activity) {
            TextView activityNameTv = itemView.findViewById(R.id.activity_name);
            TextView activityDateTv = itemView.findViewById(R.id.activity_date);
            TextView activityDurationTv = itemView.findViewById(R.id.activity_duration);
            TextView activityCaloriesTv = itemView.findViewById(R.id.activity_calories);

            String activityDuration = Integer.toString(activity.getDuration());
            String caloriesBurned = Double.toString(round(activity.getCaloriesBurned(), 1));

            activityNameTv.setText(activity.getName());
            activityDateTv.setText(activity.getDate());
            activityDurationTv.setText(context.getString(R.string.activity_duration, activityDuration, activity.getDurationUnit()));
            activityCaloriesTv.setText(context.getString(R.string.activity_calories, caloriesBurned));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    historyAdapterListener.onActivityItemClick(activity);
                }
            });
        }

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }
    }

    public interface HistoryAdapterListener {
        void onActivityItemClick(Activity activity);
    }
}
