package com.idyllic.activitytracker.data.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class Activity {
    private int id;
    private String date;
    private String name;
    private int namePosition;
    private int timesPerformed;
    private int duration;
    private String durationUnit;
    private String additionalNotes;
    private double caloriesBurned;
    private String userEmail;

    public Activity(int id, String date, String name, int namePosition, int timesPerformed, int duration, String durationUnit, String additionalNotes, double caloriesBurned, String userEmail) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.namePosition = namePosition;
        this.timesPerformed = timesPerformed;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.additionalNotes = additionalNotes;
        this.caloriesBurned = caloriesBurned;
        this.userEmail = userEmail;
    }

    // DiffUtil class implementation. Source: https://hackathon-blog-42.medium.com/listadapter-renewed-9b5b496198e2
    public static DiffUtil.ItemCallback<Activity> DIFF_CALLBACK = new DiffUtil.ItemCallback<Activity>() {
        @Override
        public boolean areItemsTheSame(@NonNull Activity oldItem, @NonNull Activity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Activity oldItem, @NonNull Activity newItem) {
            return oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getTimesPerformed() == newItem.getTimesPerformed() &&
                    oldItem.getDuration() == newItem.getDuration() &&
                    oldItem.getDurationUnit().equals(newItem.getDurationUnit()) &&
                    oldItem.getAdditionalNotes().equals(newItem.getAdditionalNotes()) &&
                    oldItem.getCaloriesBurned() == newItem.getCaloriesBurned();
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNamePosition() {
        return namePosition;
    }

    public void setNamePosition(int namePosition) {
        this.namePosition = namePosition;
    }

    public int getTimesPerformed() {
        return timesPerformed;
    }

    public void setTimesPerformed(int timesPerformed) {
        this.timesPerformed = timesPerformed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
