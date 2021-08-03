package com.idyllic.activitytracker.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idyllic.activitytracker.data.db.UserDataContract.*;

import androidx.annotation.Nullable;

public class UserDataDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "userdata.db";
    public static final int DATABASE_VERSION = 1;

    public UserDataDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_INFO_TABLE = "CREATE TABLE " +
                UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_HEIGHT + " DOUBLE NOT NULL, " +
                UserEntry.COLUMN_HEIGHT_UNIT + " TEXT NOT NULL DEFAULT 'cm', " +
                UserEntry.COLUMN_AGE + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_WEIGHT + " DOUBLE NOT NULL, " +
                UserEntry.COLUMN_WEIGHT_UNIT + " TEXT NOT NULL DEFAULT 'kg', " +
                UserEntry.COLUMN_EMAIL + " TEXT NOT NULL" +
                "); ";
        String CREATE_USER_ACTIVITIES_TABLE = "CREATE TABLE " +
                UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserActivitiesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                UserActivitiesEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserActivitiesEntry.COLUMN_NAME_POSITION + " INTEGER NOT NULL, " +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + " INTEGER NOT NULL, " +
                UserActivitiesEntry.COLUMN_DURATION + " INTEGER NOT NULL, " +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + " TEXT NOT NULL DEFAULT 'min', " +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + " TEXT NOT NULL DEFAULT 'No additional notes', " +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + " DOUBLE NOT NULL, " +
                UserActivitiesEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL" +
                ");";

        String SELECT_DEFAULT_USER = "INSERT INTO " + UserEntry.TABLE_NAME + " (" +
                UserEntry.COLUMN_NAME + "," +
                UserEntry.COLUMN_HEIGHT + "," +
                UserEntry.COLUMN_HEIGHT_UNIT + "," +
                UserEntry.COLUMN_AGE + "," +
                UserEntry.COLUMN_WEIGHT + "," +
                UserEntry.COLUMN_WEIGHT_UNIT + "," +
                UserEntry.COLUMN_EMAIL + ") " + " VALUES" + "(" +
                "'John', 188, 'cm', 40, 80, 'kg', 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_1 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'10/12/2020', 'Biking', '0', 40, 10, 'min', 'qwerty', 100, 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_2 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'11/12/2020', 'Biking', '0', 40, 10, 'min', 'qwerty', 100, 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_3 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'12/12/2020', 'Biking', '0', 40, 10, 'min', 'qwerty', 100, 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_4 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'13/12/2020', 'Biking', '0', 40, 10, 'min', 'qwerty', 100, 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_5 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'14/12/2020', 'Biking', '0', 40, 10, 'min', 'qwerty', 100, 'wiut7986@gmail.com');";

        String SELECT_DEFAULT_VALUES_6 = "INSERT INTO " + UserActivitiesEntry.TABLE_NAME + " (" +
                UserActivitiesEntry.COLUMN_DATE + "," +
                UserActivitiesEntry.COLUMN_NAME + "," +
                UserActivitiesEntry.COLUMN_NAME_POSITION + "," +
                UserActivitiesEntry.COLUMN_TIMES_PERFORMED + "," +
                UserActivitiesEntry.COLUMN_DURATION + "," +
                UserActivitiesEntry.COLUMN_DURATION_UNIT + "," +
                UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES + "," +
                UserActivitiesEntry.COLUMN_CALORIES_BURNED + "," +
                UserActivitiesEntry.COLUMN_USER_EMAIL + ") " + " VALUES" + "(" +
                "'15/12/2020', 'Biking', '0', 40, 80, 'min', 'qwerty', 2000, 'wiut7986@gmail.com');";

        sqLiteDatabase.execSQL(CREATE_USER_INFO_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_ACTIVITIES_TABLE);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_USER);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_1);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_2);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_3);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_4);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_5);
        sqLiteDatabase.execSQL(SELECT_DEFAULT_VALUES_6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
