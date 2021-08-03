package com.idyllic.activitytracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idyllic.activitytracker.data.models.Activity;

public class SharedViewModel extends AndroidViewModel {

    private final MutableLiveData<Activity> selectedActivity = new MutableLiveData<>();

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public void selectActivity(Activity activity) {
        selectedActivity.setValue(activity);
    }

    public LiveData<Activity> getSelectedActivity() {
        return selectedActivity;
    }
}
