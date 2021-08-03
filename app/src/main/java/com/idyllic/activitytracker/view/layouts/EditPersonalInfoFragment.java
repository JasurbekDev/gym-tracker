package com.idyllic.activitytracker.view.layouts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.idyllic.activitytracker.data.models.User;
import com.idyllic.activitytracker.viewmodel.UserViewModel;

public class EditPersonalInfoFragment extends Fragment {

    private EditText nameEt;
    private EditText heightEt;
    private Spinner heightSpinner;
    private EditText ageEt;
    private EditText weightEt;
    private Spinner weightSpinner;
    private Button editInfoButton;
    private Toolbar toolbar;
    private TextView toolbarText;

    private TextView nameErrorText;
    private TextView heightErrorText;
    private TextView ageErrorText;
    private TextView weightErrorText;

    private String email;
    private SQLiteDatabase database;
    private UserViewModel userViewModel;

    private String name;
    private double height;
    private String heightUnit;
    private int age;
    private double weight;
    private String weightUnit;
    private User editUser;

    private boolean isUserExist = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Setting fragment theme. Source: https://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.HomeTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_edit_personal_info, container, false);

        database = new UserDataDBHelper(getContext()).getReadableDatabase();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        nameEt = view.findViewById(R.id.name_et);
        heightEt = view.findViewById(R.id.height_et);
        heightSpinner = view.findViewById(R.id.height_spinner);
        ageEt = view.findViewById(R.id.age_et);
        weightEt = view.findViewById(R.id.weight_et);
        weightSpinner = view.findViewById(R.id.weight_spinner);
        editInfoButton = view.findViewById(R.id.edit_info_btn);
        toolbar = view.findViewById(R.id.edit_personal_info_toolbar);

        nameErrorText = view.findViewById(R.id.name_error_text);
        heightErrorText = view.findViewById(R.id.height_error_text);
        ageErrorText = view.findViewById(R.id.age_error_text);
        weightErrorText = view.findViewById(R.id.weight_error_text);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbar_text);
        toolbarText.setText(R.string.edit_personal_info);

        email = EditPersonalInfoFragmentArgs.fromBundle(getArguments()).getUserEmail();

        setupSpinners();
        setupInputFields();

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideErrorTexts();
                if (validateUserInputs()) {
                    String name = nameEt.getText().toString();
                    double height = Double.parseDouble(heightEt.getText().toString());
                    String heightUnit = heightSpinner.getSelectedItem().toString();
                    int age = Integer.parseInt(ageEt.getText().toString());
                    double weight = Double.parseDouble(weightEt.getText().toString());
                    String weightUnit = weightSpinner.getSelectedItem().toString();

                    editUser = new User(name, height, heightUnit, age, weight, weightUnit);
                    editUser.setEmail(email);

                    if (isUserExist) {
                        userViewModel.updateUserInfo(editUser);
                    } else {
                        userViewModel.createUser(editUser);
                    }

                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        return view;
    }

    private void hideErrorTexts() {
        nameErrorText.setVisibility(View.INVISIBLE);
        heightErrorText.setVisibility(View.INVISIBLE);
        ageErrorText.setVisibility(View.INVISIBLE);
        weightErrorText.setVisibility(View.INVISIBLE);
    }

    private boolean validateUserInputs() {
        boolean isValid = true;
        if (nameEt.getText().toString().trim().isEmpty()) {
            nameErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (heightEt.getText().toString().isEmpty()) {
            heightErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (ageEt.getText().toString().isEmpty()) {
            ageErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (weightEt.getText().toString().isEmpty()) {
            weightErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> heightSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.height_units, R.layout.spinner_item);
        ArrayAdapter<CharSequence> weightSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.weight_units, R.layout.spinner_item);
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        weightSpinner.setAdapter(weightSpinnerAdapter);
    }

    private void setupInputFields() {
        userViewModel.getUserByEmail(email).observe(getViewLifecycleOwner(), user -> {
            if (user.getHeightUnit() != null) {
                nameEt.setText(user.getName());
                heightEt.setText(user.getHeight() + "");
                heightSpinner.setSelection((user.getHeightUnit().equals("cm")) ? 0 : 1);
                ageEt.setText(user.getAge() + "");
                weightEt.setText(user.getWeight() + "");
                weightSpinner.setSelection((user.getWeightUnit().equals("kg")) ? 0 : 1);
            } else {
                isUserExist = false;
            }
        });
    }
}
