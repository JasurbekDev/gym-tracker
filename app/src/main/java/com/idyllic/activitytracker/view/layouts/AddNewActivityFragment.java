package com.idyllic.activitytracker.view.layouts;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.idyllic.activitytracker.data.db.UserDataContract.*;
import com.idyllic.activitytracker.data.models.Activity;
import com.idyllic.activitytracker.utils.Constants;
import com.idyllic.activitytracker.view.DatePickerFragment;
import com.idyllic.activitytracker.viewmodel.ActivityViewModel;
import com.idyllic.activitytracker.viewmodel.SharedViewModel;

public class AddNewActivityFragment extends Fragment implements DatePickerFragment.DateSelectedListener {

    private Spinner activitySpinner;
    private Spinner durationSpinner;
    private SQLiteDatabase database;

    private TextView dateErrorTv;
    private TextView timesPerformedErrorTv;
    private TextView durationErrorTv;

    private TextView dateTv;
    private EditText timesPerformedEt;
    private EditText durationEt;
    private EditText additionalNotesEt;
    private String email;
    private double weight;
    private String weightUnit;
    private int year;
    private int month;
    private int day;

    private Toolbar addActivityToolbar;
    private Toolbar editActivityToolbar;
    private Button addActivityBtn;
    private TextView addActivityToolbarTextView;

    private ActivityViewModel activityViewModel;
    private SharedViewModel sharedViewModel;

    private boolean isEdit = false;
    private int editActivityId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setting fragment theme. Source: https://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.HomeTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_add_new_activity, container, false);

        isEdit = AddNewActivityFragmentArgs.fromBundle(getArguments()).getIsEdit();

        addActivityBtn = view.findViewById(R.id.add_new_activity_btn);

        email = AddNewActivityFragmentArgs.fromBundle(getArguments()).getUserEmail();
        weight = Double.parseDouble(AddNewActivityFragmentArgs.fromBundle(getArguments()).getUserWeight());
        weightUnit = AddNewActivityFragmentArgs.fromBundle(getArguments()).getWeightUnit();

        UserDataDBHelper dbHelper = new UserDataDBHelper(getContext());
        database = dbHelper.getWritableDatabase();

        activitySpinner = view.findViewById(R.id.activity_spinner);
        durationSpinner = view.findViewById(R.id.durationSpinner);

        dateTv = view.findViewById(R.id.date_tv);
        timesPerformedEt = view.findViewById(R.id.times_performed_et);
        durationEt = view.findViewById(R.id.duration_et);
        additionalNotesEt = view.findViewById(R.id.additional_notes_et);

        dateErrorTv = view.findViewById(R.id.date_error_text);
        timesPerformedErrorTv = view.findViewById(R.id.times_performed_error_text);
        durationErrorTv = view.findViewById(R.id.duration_error_text);

        addActivityToolbar = view.findViewById(R.id.add_new_activity_toolbar);
        editActivityToolbar = view.findViewById(R.id.edit_activity_toolbar);

        addActivityToolbarTextView = addActivityToolbar.findViewById(R.id.toolbar_text);
        addActivityToolbarTextView.setText(R.string.add_new_activity);
        ((AppCompatActivity) getActivity()).setSupportActionBar(addActivityToolbar);

        setupSpinners(view);

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment(AddNewActivityFragment.this).show(getFragmentManager(), "datePicker");
            }
        });


        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    hideErrorTexts();
                    if (validateUserInputs()) {
                        String date = dateTv.getText().toString();
                        String name = activitySpinner.getSelectedItem().toString();
                        int namePosition = activitySpinner.getSelectedItemPosition();
                        int timesPerformed = Integer.parseInt(timesPerformedEt.getText().toString());
                        int duration = Integer.parseInt(durationEt.getText().toString());
                        String durationUnit = durationSpinner.getSelectedItem().toString();
                        double caloriesBurned = getCaloriesBurned(name, weight, weightUnit, duration, durationUnit);
                        String additionalNotes = additionalNotesEt.getText().toString();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserActivitiesEntry.COLUMN_DATE, date);
                        contentValues.put(UserActivitiesEntry.COLUMN_NAME, name);
                        contentValues.put(UserActivitiesEntry.COLUMN_NAME_POSITION, namePosition);
                        contentValues.put(UserActivitiesEntry.COLUMN_TIMES_PERFORMED, timesPerformed);
                        contentValues.put(UserActivitiesEntry.COLUMN_DURATION, duration);
                        contentValues.put(UserActivitiesEntry.COLUMN_DURATION_UNIT, durationUnit);
                        contentValues.put(UserActivitiesEntry.COLUMN_ADDITIONAL_NOTES, additionalNotes);
                        contentValues.put(UserActivitiesEntry.COLUMN_CALORIES_BURNED, caloriesBurned);
                        contentValues.put(UserActivitiesEntry.COLUMN_USER_EMAIL, email);

                        database.insert(UserActivitiesEntry.TABLE_NAME, null, contentValues);

                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                    }
                } else {
                    String date = dateTv.getText().toString();
                    String name = activitySpinner.getSelectedItem().toString();
                    int namePosition = activitySpinner.getSelectedItemPosition();
                    int timesPerformed = Integer.parseInt(timesPerformedEt.getText().toString());
                    int duration = Integer.parseInt(durationEt.getText().toString());
                    String durationUnit = durationSpinner.getSelectedItem().toString();
                    double caloriesBurned = getCaloriesBurned(name, weight, weightUnit, duration, durationUnit);
                    String additionalNotes = additionalNotesEt.getText().toString();
                    Activity updatedActivity = new Activity(editActivityId, date, name, namePosition, timesPerformed, duration, durationUnit, additionalNotes, caloriesBurned, email);
                    activityViewModel.updateActivity(updatedActivity);
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getSelectedActivity().observe(getViewLifecycleOwner(), activity -> {
            if (isEdit) {
                addActivityToolbar.setVisibility(View.GONE);
                editActivityToolbar.setVisibility(View.VISIBLE);
                setupLayoutForEdit(activity);
            }
        });

        return view;
    }

    private void setupLayoutForAdd() {
        addActivityBtn.setText(getString(R.string.add_new_activity));
        dateTv.setText(getString(R.string.click_here));
        activitySpinner.setSelection(0);
        timesPerformedEt.setText(getString(R.string.type_here));
        durationEt.setText(getString(R.string.type_here));
        additionalNotesEt.setText(getString(R.string.type_here));
    }

    private void setupLayoutForEdit(Activity activity) {
        addActivityBtn.setText(getString(R.string.edit_activity));
        dateTv.setTextColor(getResources().getColor(R.color.white));
        dateTv.setText(activity.getDate());
        activitySpinner.setSelection(activity.getNamePosition());
        timesPerformedEt.setText(activity.getTimesPerformed() + "");
        durationEt.setText(activity.getDuration() + "");
        additionalNotesEt.setText(activity.getAdditionalNotes());
        editActivityId = activity.getId();

        ImageView deleteIcon = editActivityToolbar.findViewById(R.id.edit_activity_delete_icon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to delete this activity?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteActivity(editActivityId);
                            }
                        })
                        .create().show();
            }
        });
    }

    private void deleteActivity(int editActivityId) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{editActivityId + ""};
        database.delete(UserActivitiesEntry.TABLE_NAME, whereClause, whereArgs);
        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).popBackStack();
    }

    private double getCaloriesBurned(String name, double weight, String weightUnit, int duration, String durationUnit) {
        double activityMETValue = Constants.getActivitiesToMETValues().get(name);
        double weightInKg = weight;
        if (weightUnit.equals("lb")) {
            weightInKg = weight * 0.453592;
        }
        double durationInHours = duration;
        if (durationUnit.equals("min")) {
            durationInHours = (double) duration / 60;
        }
        return activityMETValue * weightInKg * durationInHours;
    }

    @Override
    public void onDateSelected(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        dateTv.setTextColor(getResources().getColor(R.color.white));
        dateTv.setText(day + "/" + month + "/" + year);
    }

    private boolean validateUserInputs() {
        boolean isValid = true;

        if (dateTv.getText().toString().equals(getString(R.string.click_here))) {
            dateErrorTv.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (timesPerformedEt.getText().toString().isEmpty()) {
            timesPerformedErrorTv.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (durationEt.getText().toString().isEmpty()) {
            durationErrorTv.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void hideErrorTexts() {
        dateErrorTv.setVisibility(View.INVISIBLE);
        timesPerformedErrorTv.setVisibility(View.INVISIBLE);
        durationErrorTv.setVisibility(View.INVISIBLE);
    }

    private void setupSpinners(View view) {
        ArrayAdapter<CharSequence> activitySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.activities, R.layout.spinner_item);
        ArrayAdapter<CharSequence> durationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.duration_units, R.layout.spinner_item);
        activitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activitySpinnerAdapter);
        durationSpinner.setAdapter(durationSpinnerAdapter);
    }

}
