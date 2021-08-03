package com.idyllic.activitytracker.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static final Map<String, Double> activitiesToMETValues = new HashMap<>();

    public Constants() {
        activitiesToMETValues.put("Biking", 7.5);
        activitiesToMETValues.put("Push ups", 3.8);
        activitiesToMETValues.put("Sit ups", 3.8);
        activitiesToMETValues.put("Pull ups", 3.8);
        activitiesToMETValues.put("Lunges", 3.8);
        activitiesToMETValues.put("Rope skipping", 11.0);
        activitiesToMETValues.put("Water exercise", 5.3);
        activitiesToMETValues.put("Badminton", 5.5);
        activitiesToMETValues.put("Basketball", 8.0);
        activitiesToMETValues.put("Bowling", 3.0);
        activitiesToMETValues.put("Football", 8.0);
        activitiesToMETValues.put("Juggling", 4.0);
        activitiesToMETValues.put("Skateboarding", 5.0);
        activitiesToMETValues.put("Soccer", 10.0);
        activitiesToMETValues.put("Tennis", 7.3);
    }

    public static Map<String, Double> getActivitiesToMETValues() {
        return activitiesToMETValues;
    }
}
