package com.aar.app.todomemory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aar.app.todomemory.edittodo.ToDoEditorFragment;
import com.aar.app.todomemory.history.HistoryFragment;
import com.aar.app.todomemory.settings.SettingsFragment;
import com.aar.app.todomemory.settings.SettingsProvider;
import com.aar.app.todomemory.todolist.ToDoListFragment;

public class MainActivity extends AppCompatActivity
        implements SettingsFragment.OnThemeChanged, ToDoListFragment.OnNavigationClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsProvider settings = SettingsProvider.getInstance(getApplication());
        int themeRes = settings.theme();
        setTheme(themeRes);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            goToToDoList();
        }
    }

    @Override
    public void onThemeChanged(int themeRes) {
        recreate();
    }

    @Override
    public void onGoToAdd() {
        goToToDoEditor(0);
    }

    @Override
    public void onGoToEdit(long todoId) {
        goToToDoEditor(todoId);
    }

    @Override
    public void onGoToHistory() {
        replaceFragment(new HistoryFragment(), true, true);
    }

    @Override
    public void onGoToSettings() {
        SettingsFragment fragment = new SettingsFragment();
        replaceFragment(fragment, true, true);
    }

    private void goToToDoList() {
        replaceFragment(new ToDoListFragment(), true, false);
    }

    private void goToToDoEditor(long todoId) {
        ToDoEditorFragment fragment = ToDoEditorFragment.newInstance(todoId);
        fragment.setOnSaveSuccessfullyListener(this::onBackPressed);
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
    }
}
