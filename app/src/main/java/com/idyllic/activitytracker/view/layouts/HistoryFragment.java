package com.idyllic.activitytracker.view.layouts;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idyllic.activitytracker.R;
import com.idyllic.activitytracker.data.db.UserDataDBHelper;
import com.idyllic.activitytracker.data.models.Activity;
import com.idyllic.activitytracker.view.adapters.HistoryAdapter;
import com.idyllic.activitytracker.viewmodel.ActivityViewModel;
import com.idyllic.activitytracker.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements HistoryAdapter.HistoryAdapterListener {

    private SQLiteDatabase database;
    private HistoryAdapter historyAdapter;
    private RecyclerView historyRv;
    private String email;
    private Toolbar toolbar;
    private TextView toolbarTv;
    private ImageView toolbarSearchIcon;
    private SearchView toolbarSearchView;
    private ImageView svCloseButton;

    private ActivityViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private boolean isSvOpen = false;
    private List<Activity> activityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Setting fragment theme. Source: https://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.HomeTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view = localInflater.inflate(R.layout.fragment_history, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        historyRv = view.findViewById(R.id.history_rv);

        email = HistoryFragmentArgs.fromBundle(getArguments()).getUserEmail();

        UserDataDBHelper dbHelper = new UserDataDBHelper(getContext());
        database = dbHelper.getReadableDatabase();


        historyAdapter = new HistoryAdapter(getContext(), this);
        historyRv.setAdapter(historyAdapter);
        historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRv.setHasFixedSize(true);

        viewModel.getActivitiesByEmail(email).observe(getViewLifecycleOwner(), activities -> {
            historyAdapter.submitList(activities);
            activityList = activities;
        });

        toolbar = view.findViewById(R.id.history_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbarTv = toolbar.findViewById(R.id.toolbar_text);
        toolbarSearchIcon = toolbar.findViewById(R.id.history_search_icon);
        toolbarSearchView = toolbar.findViewById(R.id.history_sv);
        svCloseButton = toolbar.findViewById(R.id.sv_close_btn);
        toolbarTv.setText(R.string.history);
        setupSearchView();

        // Handling back press from fragment. Source: https://proandroiddev.com/backpress-handling-in-android-fragments-the-old-and-the-new-method-c41d775fb776
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isSvOpen) {
                    hideSearchView();
                } else {
                    setEnabled(false);
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityItemClick(Activity activity) {
        sharedViewModel.selectActivity(activity);
        HistoryFragmentDirections.ActionHistoryFragmentToAddNewActivityFragment action = HistoryFragmentDirections.actionHistoryFragmentToAddNewActivityFragment(true);
        action.setUserEmail(activity.getUserEmail());
        Navigation.findNavController(getView()).navigate(action);
    }

    private void setupSearchView() {
        toolbarSearchView.setIconifiedByDefault(false);

        toolbarSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchView();
                showCloseButton();
            }
        });

        // Handling SearchView's clear text button. Source: https://stackoverflow.com/questions/24794377/how-do-i-capture-searchviews-clear-button-click
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        // Assumes current activity is the searchable activity
        toolbarSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        // Get the search close button image view
        ImageView clearTextButton = (ImageView) toolbarSearchView.findViewById(R.id.search_close_btn);

        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseButton();

                //Find EditText view
                EditText et = (EditText) getActivity().findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");

                //Clear query
                toolbarSearchView.setQuery("", false);
            }
        });

        svCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toolbarSearchView.onActionViewCollapsed();
                hideSearchView();
            }
        });

        toolbarSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchView();
                return true;
            }
        });

        toolbarSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() && isSvOpen) {
                    showCloseButton();
                } else {
                    hideCloseButton();
                }

                List<Activity> queryList = new ArrayList<>();
                for (Activity activity : activityList) {
                    if (activity.getName().toLowerCase().contains(newText.toLowerCase())) {
                        queryList.add(activity);
                    }
                }

                historyAdapter.submitList(queryList);
                return true;
            }
        });
    }

    private void showCloseButton() {
        svCloseButton.setVisibility(View.VISIBLE);
    }

    private void hideCloseButton() {
        svCloseButton.setVisibility(View.GONE);
    }

    private void showSearchView() {
        toolbarSearchView.onActionViewExpanded();
        toolbarTv.setVisibility(View.GONE);
        toolbarSearchIcon.setVisibility(View.GONE);
        toolbarSearchView.setVisibility(View.VISIBLE);
        isSvOpen = true;
    }

    private void hideSearchView() {
        toolbarSearchView.onActionViewCollapsed();
        toolbarTv.setVisibility(View.VISIBLE);
        toolbarSearchIcon.setVisibility(View.VISIBLE);
        toolbarSearchView.setVisibility(View.GONE);
        hideCloseButton();
        isSvOpen = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityList.clear();
    }
}
