package com.aar.app.todomemory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aar.app.todomemory.edittodo.ToDoEditorFragment;
import com.aar.app.todomemory.history.HistoryFragment;
import com.aar.app.todomemory.settings.SettingsFragment;
import com.aar.app.todomemory.settings.SettingsProvider;
import com.aar.app.todomemory.todolist.ToDoListFragment;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnThemeChanged {

    private View mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsProvider settings = SettingsProvider.getInstance(getApplication());
        int themeRes = settings.theme();
        setTheme(themeRes);

        setContentView(R.layout.activity_main);

        mBottomNavigation = findViewById(R.id.bottomButtons);

        if (savedInstanceState == null) {
            goToToDoList();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            hideNavigationButtons();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                showNavigationButtons();
            }
        });
    }

    @Override
    public void onThemeChanged(int themeRes) {
        recreate();
    }

    public void onHistoryButtonClicked(View view) {
        goToHistory();
    }

    public void onAddButtonClicked(View view) {
        goToToDoEditor(0);
    }

    public void onSettingsButtonClicked(View view) {
        goToSettings();
    }

    private void goToToDoList() {
        ToDoListFragment fragment = new ToDoListFragment();
        fragment.setOnToDoClickListener(todo -> goToToDoEditor(todo.getId()));
        replaceFragment(fragment, true, false);
        showNavigationButtons();
    }

    private void goToToDoEditor(long todoId) {
        ToDoEditorFragment fragment = ToDoEditorFragment.newInstance(todoId);
        fragment.setOnSaveSuccessfullyListener(this::onBackPressed);
        replaceFragment(fragment, true, true);
    }

    private void goToHistory() {
        replaceFragment(new HistoryFragment(), true, true);
    }

    private void goToSettings() {
        SettingsFragment fragment = new SettingsFragment();
        replaceFragment(fragment, true, true);
    }

    private void replaceFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }

        transaction.replace(R.id.container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
        if (fragment instanceof ToDoListFragment) {
            showNavigationButtons();
        } else {
            hideNavigationButtons();
        }
    }

    private void hideNavigationButtons() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    private void showNavigationButtons() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

}
