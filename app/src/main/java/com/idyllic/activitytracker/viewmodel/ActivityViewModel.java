package com.idyllic.activitytracker.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idyllic.activitytracker.data.db.UserDataContract;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.idyllic.activitytracker.data.models.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewModel extends AndroidViewModel {

    private SQLiteDatabase database;
    private MutableLiveData<List<Activity>> activitiesByEmail;

    public ActivityViewModel(Application context) {
        super(context);
        database = new UserDataDBHelper(context).getReadableDatabase();
    }

    public LiveData<List<Activity>> getActivitiesByEmail(String email) {
        if (activitiesByEmail == null) {
            activitiesByEmail = new MutableLiveData<List<Activity>>();
        }
        return loadActivitiesByEmail(email);
    }

    public LiveData<List<Activity>> loadActivitiesByEmail(String email) {
        List<Activity> activities = new ArrayList<>();

        String selection = UserDataContract.UserActivitiesEntry.COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = new String[]{email};
        Cursor cursor = database.query(UserDataContract.UserActivitiesEntry.TABLE_NAME, null, selection, selectionArgs, null, null, "_ID DESC");

        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry._ID));
            String date = cursor.getString(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_DATE));
            String name = cursor.getString(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_NAME));
            int namePosition = cursor.getInt(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_NAME_POSITION));
            int timesPerformed = cursor.getInt(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_TIMES_PERFORMED));
            int duration = cursor.getInt(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_DURATION));
            String durationUnit = cursor.getString(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_DURATION_UNIT));
            String additionalNotes = cursor.getString(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES));
            double caloriesBurned = cursor.getDouble(cursor.getColumnIndex(UserDataContract.UserActivitiesEntry.COLUMN_CALORIES_BURNED));

            Activity activity = new Activity(id, date, name, namePosition, timesPerformed, duration, durationUnit, additionalNotes, caloriesBurned, email);
            activities.add(activity);
        }
        cursor.close();

        activitiesByEmail.postValue(activities);

        return activitiesByEmail;
    }

    public void updateActivity(Activity activity) {
        updateUserActivity(activity);
    }

    private void updateUserActivity(Activity activity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_DATE, activity.getDate());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_NAME, activity.getName());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_NAME_POSITION, activity.getNamePosition());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_TIMES_PERFORMED, activity.getTimesPerformed());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_DURATION, activity.getDuration());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_DURATION_UNIT, activity.getDurationUnit());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES, activity.getAdditionalNotes());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_CALORIES_BURNED, activity.getCaloriesBurned());
        contentValues.put(UserDataContract.UserActivitiesEntry.COLUMN_USER_EMAIL, activity.getUserEmail());

        String whereClause = "_id=?";
        String[] whereArgs = new String[]{activity.getId() + ""};

        database.update(UserDataContract.UserActivitiesEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
    }
}
