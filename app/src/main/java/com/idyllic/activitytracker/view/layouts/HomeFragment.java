package com.idyllic.activitytracker.view.layouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.idyllic.activitytracker.data.db.UserDataContract.*;
import com.idyllic.activitytracker.data.models.Activity;
import com.idyllic.activitytracker.viewmodel.ActivityViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTv;
    private TextView heightTv;
    private TextView ageTv;
    private TextView weightTv;
    private String email;
    private DrawerLayout drawer;
    private ImageView navMenuIcon;
    private SharedPreferences sharedPreferences;
    private double weight;
    private String weightUnit;
    private BarChart chart;
    private ActivityViewModel activityViewModel;

    private SQLiteDatabase mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);

        // Handling back press from fragment. Source: https://proandroiddev.com/backpress-handling-in-android-fragments-the-old-and-the-new-method-c41d775fb776
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getActivity().onBackPressed();
                }
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Setting fragment theme. Source: https://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.HomeTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_home, container, false);

        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = view.findViewById(R.id.home_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        UserDataDBHelper dbHelper = new UserDataDBHelper(getContext());
        mDatabase = dbHelper.getReadableDatabase();

        nameTv = view.findViewById(R.id.user_name_tv);
        heightTv = view.findViewById(R.id.user_height_tv);
        ageTv = view.findViewById(R.id.user_age_tv);
        weightTv = view.findViewById(R.id.user_weight_tv);
        drawer = view.findViewById(R.id.home_drawer_layout);
        navMenuIcon = view.findViewById(R.id.navigation_menu_icon);
        chart = view.findViewById(R.id.bar_chart);

        Button addNewActivityBtn = view.findViewById(R.id.add_new_activity_btn);

        addNewActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionHomeFragmentToAddNewActivityFragment action = HomeFragmentDirections.actionHomeFragmentToAddNewActivityFragment(false);
                if (weight != 0.0) {
                    action.setUserWeight(Double.toString(weight));
                }
                if (weightUnit != null) {
                    action.setWeightUnit(weightUnit);
                }
                action.setUserEmail(email);
                Navigation.findNavController(view).navigate(action);
            }
        });

        email = HomeFragmentArgs.fromBundle(getArguments()).getUserEmail();

        setUserInfos();

        navMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        activityViewModel.getActivitiesByEmail(email).observe(getViewLifecycleOwner(), this::setupBarChart);

        return view;
    }

    private void setupBarChart(List<Activity> activityList) {
        List<BarEntry> calories = new ArrayList();

        int caloryIndex = 0;
        for (Activity activity : activityList) {
            String calory = Double.toString(activity.getCaloriesBurned());
            calories.add(new BarEntry(Float.parseFloat(calory), caloryIndex++));
        }

        ArrayList dates = new ArrayList();

        for (Activity activity : activityList) {
            String[] dateParts = activity.getDate().split("/");
            String date = dateParts[0] + "/" + dateParts[1];
            dates.add(date);
        }

        int white = getResources().getColor(R.color.white);
        BarDataSet bardataset = new BarDataSet(calories, "Calories burned");
        bardataset.setColor(white);

        chart.animateY(1000);
        BarData data = new BarData(dates, bardataset);
        data.setValueTextColor(getResources().getColor(R.color.white));
        chart.setDescriptionColor(getResources().getColor(R.color.white));
        chart.setGridBackgroundColor(getResources().getColor(R.color.chart_border));

        chart.getXAxis().setTextColor(white);
        chart.getLegend().setTextColor(white);

        bardataset.setColor(getResources().getColor(R.color.green));
        chart.setData(data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_personal_info:
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToEditPersonalInfoFragment(email));
                break;
            case R.id.history:
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment(email));
                break;
            case R.id.share_with_others:
                break;
            case R.id.log_out:
                sharedPreferences.edit().putBoolean("keepLoggedIn", false).apply();
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionHomeFragmentToSplashScreenFragment());
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setUserInfos() {
        String selection = UserEntry.COLUMN_EMAIL + "=?";
        String[] selectionArgs = new String[]{email};

        Cursor cursor = mDatabase.query(UserEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME));
            double height = cursor.getDouble(cursor.getColumnIndex(UserEntry.COLUMN_HEIGHT));
            String heightUnit = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_HEIGHT_UNIT));
            int age = cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_AGE));
            weight = cursor.getDouble(cursor.getColumnIndex(UserEntry.COLUMN_WEIGHT));
            weightUnit = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_WEIGHT_UNIT));

            nameTv.setText(name);
            heightTv.setText(getString(R.string.user_height_value, Double.toString(height) + " " + heightUnit));
            ageTv.setText(Integer.toString(age));
            weightTv.setText(getString(R.string.user_weight_value, Double.toString(weight) + " " + weightUnit));
        }

        if (cursor.getCount() == 0) {
            nameTv.setText(R.string.not_available);
            heightTv.setText(getString(R.string.not_available));
            ageTv.setText(R.string.not_available);
            weightTv.setText(R.string.not_available);
        }

        cursor.close();
    }
}
