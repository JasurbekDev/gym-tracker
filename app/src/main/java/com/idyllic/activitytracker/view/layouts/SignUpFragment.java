package com.idyllic.activitytracker.view.layouts;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.idyllic.activitytracker.data.db.UserDataContract.*;

public class SignUpFragment extends Fragment {

    private EditText nameEt;
    private EditText heightEt;
    private EditText ageEt;
    private EditText weightEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;

    private String name;
    private double height;
    private String heightUnit;
    private int age;
    private double weight;
    private String weightUnit;
    private String email;
    private String password;
    private String confirmPassword;

    private TextView nameErrorText;
    private TextView heightErrorText;
    private TextView ageErrorText;
    private TextView weightErrorText;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private TextView confirmPasswordErrorText;
    private RelativeLayout progressBarContainer;
    private Spinner heightSpinner;
    private Spinner weightSpinner;

    private FirebaseAuth auth;
    private SQLiteDatabase mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        UserDataDBHelper dbHelper = new UserDataDBHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();

        auth = FirebaseAuth.getInstance();

        nameEt = view.findViewById(R.id.user_name_edittext);
        heightEt = view.findViewById(R.id.user_height_edittext);
        ageEt = view.findViewById(R.id.user_age_edittext);
        weightEt = view.findViewById(R.id.user_weight_edittext);
        emailEt = view.findViewById(R.id.user_email_edittext);
        passwordEt = view.findViewById(R.id.user_password_edittext);
        confirmPasswordEt = view.findViewById(R.id.user_confirm_password_edittext);

        nameErrorText = view.findViewById(R.id.user_name_error_text);
        heightErrorText = view.findViewById(R.id.user_height_error_text);
        ageErrorText = view.findViewById(R.id.user_age_error_text);
        weightErrorText = view.findViewById(R.id.user_weight_error_text);
        emailErrorText = view.findViewById(R.id.user_email_error_text);
        passwordErrorText = view.findViewById(R.id.user_password_error_text);
        confirmPasswordErrorText = view.findViewById(R.id.user_confirm_password_error_text);
        progressBarContainer = view.findViewById(R.id.sign_up_progress_bar);
        heightSpinner = view.findViewById(R.id.sign_up_height_spinner);
        weightSpinner = view.findViewById(R.id.sign_up_weight_spinner);

        Button signUpButton = view.findViewById(R.id.sign_up_button);
        TextView haveAccountTv = view.findViewById(R.id.have_account_tv);

        ClickableSpan openLogInPage = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                NavDirections action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment();
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.dark_pink));
                ds.setUnderlineText(false);
            }
        };

        SpannableString spannableString = new SpannableString(getString(R.string.have_an_account));
        spannableString.setSpan(openLogInPage, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        haveAccountTv.setText(spannableString);
        haveAccountTv.setMovementMethod(LinkMovementMethod.getInstance());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideErrorTexts();
                if (validateUserInputs()) {
                    name = nameEt.getText().toString().trim();
                    height = Double.parseDouble(heightEt.getText().toString());
                    heightUnit = heightSpinner.getSelectedItem().toString();
                    age = Integer.parseInt(ageEt.getText().toString());
                    weight = Double.parseDouble(weightEt.getText().toString());
                    weightUnit = weightSpinner.getSelectedItem().toString();
                    email = emailEt.getText().toString();
                    password = passwordEt.getText().toString();
                    confirmPassword = confirmPasswordEt.getText().toString();
                    progressBarContainer.setVisibility(View.VISIBLE);
                    signUpButton.setEnabled(false);

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putBoolean("keepLoggedIn", false).apply();
                                sharedPreferences.edit().putString("currentUserEmail", email).apply();

                                // Save info to local database
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(UserEntry.COLUMN_NAME, name);
                                contentValues.put(UserEntry.COLUMN_HEIGHT, height);
                                contentValues.put(UserEntry.COLUMN_HEIGHT_UNIT, heightUnit);
                                contentValues.put(UserEntry.COLUMN_AGE, age);
                                contentValues.put(UserEntry.COLUMN_WEIGHT, weight);
                                contentValues.put(UserEntry.COLUMN_WEIGHT_UNIT, weightUnit);
                                contentValues.put(UserEntry.COLUMN_EMAIL, email);

                                mDatabase.insert(UserEntry.TABLE_NAME, null, contentValues);

                                Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment(email));

                                progressBarContainer.setVisibility(View.GONE);
                                signUpButton.setEnabled(true);
                            } else {
                                progressBarContainer.setVisibility(View.GONE);
                                signUpButton.setEnabled(true);
                                Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        setupSpinners(view);

        return view;
    }

    private void setupSpinners(View view) {
        heightSpinner = view.findViewById(R.id.sign_up_height_spinner);
        weightSpinner = view.findViewById(R.id.sign_up_weight_spinner);
        ArrayAdapter<CharSequence> heightSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.height_units, R.layout.spinner_item);
        ArrayAdapter<CharSequence> weightSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.weight_units, R.layout.spinner_item);
        heightSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightSpinnerAdapter);
        weightSpinner.setAdapter(weightSpinnerAdapter);
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
        if (emailEt.getText().toString().isEmpty()) {
            emailErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!emailEt.getText().toString().contains("@")) {
            emailErrorText.setText(R.string.not_email);
            emailErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (passwordEt.getText().toString().isEmpty()) {
            passwordErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (passwordEt.getText().toString().length() < 6 || passwordEt.getText().toString().length() > 30) {
            passwordErrorText.setText(R.string.password_not_valid);
            passwordErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!passwordEt.getText().toString().equals(confirmPasswordEt.getText().toString())) {
            confirmPasswordErrorText.setVisibility(View.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    private void hideErrorTexts() {
        nameErrorText.setVisibility(View.INVISIBLE);
        heightErrorText.setVisibility(View.INVISIBLE);
        ageErrorText.setVisibility(View.INVISIBLE);
        weightErrorText.setVisibility(View.INVISIBLE);
        emailErrorText.setVisibility(View.INVISIBLE);
        passwordErrorText.setVisibility(View.INVISIBLE);
        confirmPasswordErrorText.setVisibility(View.INVISIBLE);
    }
}
