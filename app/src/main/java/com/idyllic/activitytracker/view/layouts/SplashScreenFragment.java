package com.idyllic.activitytracker.view.layouts;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.idyllic.activitytracker.R;

public class SplashScreenFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        Button logInButton = view.findViewById(R.id.log_in_button_splash);
        TextView dontHaveAccountTv = view.findViewById(R.id.dont_have_account_tv);

        ClickableSpan openSignUpPage = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                NavDirections action = SplashScreenFragmentDirections.actionSplashScreenFragmentToSignUpFragment();
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
                NavDirections action = SplashScreenFragmentDirections.actionSplashScreenFragmentToLogInFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }
}
