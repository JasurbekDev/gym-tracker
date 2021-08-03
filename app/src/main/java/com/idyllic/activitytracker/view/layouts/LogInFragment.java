package com.idyllic.activitytracker.view.layouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.idyllic.activitytracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInFragment extends Fragment {

    private EditText emailEt;
    private EditText passwordEt;
    private String email;
    private String password;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private RelativeLayout progressBarContainer;
    private CheckBox keepLoggedInCheckbox;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        auth = FirebaseAuth.getInstance();

        emailEt = view.findViewById(R.id.log_in_email);
        passwordEt = view.findViewById(R.id.log_in_password);

        emailErrorText = view.findViewById(R.id.log_in_email_error_text);
        passwordErrorText = view.findViewById(R.id.log_in_password_error_text);
        progressBarContainer = view.findViewById(R.id.log_in_progress_bar);
        keepLoggedInCheckbox = view.findViewById(R.id.keep_logged_checkbox);


        Button logInButton = view.findViewById(R.id.log_in_button);
        Button sampleAccountButton = view.findViewById(R.id.log_in_to_sample_account);
        TextView dontHaveAccountTv = view.findViewById(R.id.dont_have_account_tv);

        ClickableSpan openSignUpPage = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                NavDirections action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment();
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.dark_pink));
                ds.setUnderlineText(false);
            }
        };

        SpannableString spannableString = new SpannableString(getString(R.string.dont_have_an_account));
        spannableString.setSpan(openSignUpPage, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dontHaveAccountTv.setText(spannableString);
        dontHaveAccountTv.setMovementMethod(LinkMovementMethod.getInstance());

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideErrorTexts();
                if (validateUserInputs()) {
                    email = emailEt.getText().toString().trim();
                    password = passwordEt.getText().toString();
                    progressBarContainer.setVisibility(View.VISIBLE);
                    logInButton.setEnabled(false);
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putBoolean("keepLoggedIn", keepLoggedInCheckbox.isChecked()).apply();
                                sharedPreferences.edit().putString("currentUserEmail", email).apply();

                                Navigation.findNavController(view).navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment(email));

                                progressBarContainer.setVisibility(View.GONE);
                                logInButton.setEnabled(true);
                            } else {
                                progressBarContainer.setVisibility(View.GONE);
                                logInButton.setEnabled(true);
                                Toast.makeText(getContext(), "Could not log in", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        sampleAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("keepLoggedIn", keepLoggedInCheckbox.isChecked()).apply();
                sharedPreferences.edit().putString("currentUserEmail", getString(R.string.sample_email)).apply();

                Navigation.findNavController(view).navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment(getString(R.string.sample_email)));

                progressBarContainer.setVisibility(View.GONE);
                logInButton.setEnabled(true);
            }
        });

        return view;
    }

    private boolean validateUserInputs() {
        boolean isValid = true;

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
        }

        return isValid;
    }

    private void hideErrorTexts() {
        emailErrorText.setVisibility(View.INVISIBLE);
        passwordErrorText.setVisibility(View.INVISIBLE);
    }
}
