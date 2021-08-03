package com.idyllic.activitytracker.view.layouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = this.getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
            String currentUserEmail = sharedPreferences.getString("currentUserEmail", "wiut7986@gmail.com");
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = navHostFragment.getNavController();

            if (sharedPreferences.getBoolean("keepLoggedIn", false)) {
                navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment(currentUserEmail));
            }

            // Initializing activity values
            new Constants();
        }
    }
}