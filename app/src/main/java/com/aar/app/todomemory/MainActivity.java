package com.aar.app.todomemory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aar.app.todomemory.edittodo.ToDoEditorFragment;
import com.aar.app.todomemory.history.HistoryFragment;
import com.aar.app.todomemory.todolist.ToDoListFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;
    private View mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigation = findViewById(R.id.bottomButtons);

        goToToDoList();
    }

    @Override
    public void onBackPressed() {
        if (!(mCurrentFragment instanceof ToDoListFragment)) {
            goToToDoList();
        } else {
            super.onBackPressed();
        }
    }

    public void onHistoryButtonClicked(View view) {
        goToHistory();
    }

    public void onAddButtonClicked(View view) {
        goToToDoEditor(0);
    }

    public void onSettingsButtonClicked(View view) {

    }

    private void goToToDoList() {
        ToDoListFragment fragment = new ToDoListFragment();
        fragment.setOnToDoClickListener(todo -> goToToDoEditor(todo.getId()));
        replaceFragment(fragment, true);
    }

    private void goToToDoEditor(long todoId) {
        ToDoEditorFragment fragment = ToDoEditorFragment.newInstance(todoId);
        fragment.setOnSaveSuccessfullyListener(this::goToToDoList);
        replaceFragment(fragment, true);
    }

    private void goToHistory() {
        replaceFragment(new HistoryFragment(), true);
    }

    private void replaceFragment(Fragment fragment, boolean animate) {
        mCurrentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }

        transaction
                .replace(R.id.container, mCurrentFragment)
                .commit();

        if (mCurrentFragment instanceof ToDoListFragment) {
            undimNavigationButtons();
        } else {
            dimNavigationButtons();
        }
    }

    private void dimNavigationButtons() {
        mBottomNavigation.setVisibility(View.GONE);
    }

    private void undimNavigationButtons() {
        mBottomNavigation.setVisibility(View.VISIBLE);
    }

}
