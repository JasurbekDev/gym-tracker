package com.idyllic.activitytracker.data.db;

import android.provider.BaseColumns;

public class UserDataContract {

    private UserDataContract() {
    }

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "userInfo";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_HEIGHT_UNIT = "heightUnit";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_WEIGHT_UNIT = "weightUnit";
        public static final String COLUMN_EMAIL = "email";
    }

    public static final class UserActivitiesEntry implements BaseColumns {
        public static final String TABLE_NAME = "userActivities";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NAME_POSITION = "namePosition";
        public static final String COLUMN_TIMES_PERFORMED = "timesPerformed";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_DURATION_UNIT = "durationUnit";
        public static final String COLUMN_ADDITIONAL_NOTES = "additionalNotes";
        public static final String COLUMN_CALORIES_BURNED = "caloriesBurned";
        public static final String COLUMN_USER_EMAIL = "userEmail";
    }
}
