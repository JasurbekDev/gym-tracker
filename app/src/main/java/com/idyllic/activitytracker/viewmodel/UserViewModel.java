package com.idyllic.activitytracker.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idyllic.activitytracker.data.db.UserDataContract;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.idyllic.activitytracker.data.models.User;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> user;
    private SQLiteDatabase database;
    private SharedPreferences sharedPreferences;

    public UserViewModel(@NonNull Application application) {
        super(application);
        database = new UserDataDBHelper(application).getReadableDatabase();
        sharedPreferences = application.getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
    }

    public LiveData<User> getUserByEmail(String email) {
        if (user == null) {
            user = new MutableLiveData<>();
        }

        return loadUserByEmail(email);
    }

    public void createUser(User user) {
        sharedPreferences.edit().putBoolean("keepLoggedIn", false).apply();
        sharedPreferences.edit().putString("currentUserEmail", user.getEmail()).apply();

        // Save info to local database
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDataContract.UserEntry.COLUMN_NAME, user.getName());
        contentValues.put(UserDataContract.UserEntry.COLUMN_HEIGHT, user.getHeight());
        contentValues.put(UserDataContract.UserEntry.COLUMN_HEIGHT_UNIT, user.getHeightUnit());
        contentValues.put(UserDataContract.UserEntry.COLUMN_AGE, user.getAge());
        contentValues.put(UserDataContract.UserEntry.COLUMN_WEIGHT, user.getWeight());
        contentValues.put(UserDataContract.UserEntry.COLUMN_WEIGHT_UNIT, user.getWeightUnit());
        contentValues.put(UserDataContract.UserEntry.COLUMN_EMAIL, user.getEmail());

        database.insert(UserDataContract.UserEntry.TABLE_NAME, null, contentValues);
    }

    public void updateUserInfo(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDataContract.UserEntry.COLUMN_NAME, user.getName());
        contentValues.put(UserDataContract.UserEntry.COLUMN_HEIGHT, user.getHeight());
        contentValues.put(UserDataContract.UserEntry.COLUMN_HEIGHT_UNIT, user.getHeightUnit());
        contentValues.put(UserDataContract.UserEntry.COLUMN_AGE, user.getAge());
        contentValues.put(UserDataContract.UserEntry.COLUMN_WEIGHT, user.getWeight());
        contentValues.put(UserDataContract.UserEntry.COLUMN_WEIGHT_UNIT, user.getWeightUnit());

        String whereClause = UserDataContract.UserEntry.COLUMN_EMAIL + "=?";
        String[] whereArgs = new String[]{user.getEmail()};
        database.update(UserDataContract.UserEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
    }

    private LiveData<User> loadUserByEmail(String email) {
        User existingUser = new User();
        String selection = UserDataContract.UserEntry.COLUMN_EMAIL + "=?";
        String[] selectionArgs = new String[]{email};

        Cursor cursor = database.query(UserDataContract.UserEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_NAME));
            double height = cursor.getDouble(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_HEIGHT));
            String heightUnit = cursor.getString(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_HEIGHT_UNIT));
            int age = cursor.getInt(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_AGE));
            double weight = cursor.getDouble(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_WEIGHT));
            String weightUnit = cursor.getString(cursor.getColumnIndex(UserDataContract.UserEntry.COLUMN_WEIGHT_UNIT));

            existingUser = new User(name, height, heightUnit, age, weight, weightUnit);
        }

        user.postValue(existingUser);
        return user;
    }
}
