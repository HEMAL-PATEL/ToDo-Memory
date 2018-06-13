package com.aar.app.todomemory.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.Utils;

public class SettingsFragment extends Fragment {

    private HelpFragment mHelpFragment;
    private SettingsViewModel mViewModel;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwitchCompat switchRemove = view.findViewById(R.id.switchRemove);
        SwitchCompat switchHistory = view.findViewById(R.id.switchHistory);
        SwitchCompat switchRunTurnOn = view.findViewById(R.id.switchRunTurnOn);
        View alignLeft = view.findViewById(R.id.alignLeft);
        View alignCenter = view.findViewById(R.id.alignCenter);
        View alignRight = view.findViewById(R.id.alignRight);
        View orderAsc = view.findViewById(R.id.orderAsc);
        View orderDesc = view.findViewById(R.id.orderDesc);
        View btnRateApp = view.findViewById(R.id.btnRateApp);
        View btnHelp = view.findViewById(R.id.btnHelp);

        switchRemove.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setRemoveWhenDone(isChecked));
        switchHistory.setOnCheckedChangeListener(((buttonView, isChecked) -> mViewModel.setHistoryWhenDone(isChecked)));
        switchRunTurnOn.setOnCheckedChangeListener(((buttonView, isChecked) -> mViewModel.setRunWhenTurnOn(isChecked)));
        alignLeft.setOnClickListener(v -> mViewModel.setTextAlignment(SettingsProvider.ALIGN_LEFT));
        alignCenter.setOnClickListener(v -> mViewModel.setTextAlignment(SettingsProvider.ALIGN_CENTER));
        alignRight.setOnClickListener(v -> mViewModel.setTextAlignment(SettingsProvider.ALIGN_RIGHT));
        orderAsc.setOnClickListener(v -> mViewModel.setTodoOrder(SettingsProvider.ORDER_ASC));
        orderDesc.setOnClickListener(v -> mViewModel.setTodoOrder(SettingsProvider.ORDER_DESC));
        btnRateApp.setOnClickListener(this::onRateAppClick);
        btnHelp.setOnClickListener(this::onHelpClick);

        mViewModel.getRemoveWhenDone().observe(this, enable -> switchRemove.setChecked(enable == null ? false : enable));
        mViewModel.getHistoryWhenDone().observe(this, enable -> switchHistory.setChecked(enable == null ? false : enable));
        mViewModel.getRunWhenTurnOn().observe(this, enable -> switchRunTurnOn.setChecked(enable == null ? false : enable));
        mViewModel.getTextAlignment().observe(this, alignment -> {
            alignLeft.setBackgroundColor(Color.TRANSPARENT);
            alignCenter.setBackgroundColor(Color.TRANSPARENT);
            alignRight.setBackgroundColor(Color.TRANSPARENT);
            if (alignment == SettingsProvider.ALIGN_LEFT) alignLeft.setBackgroundColor(Color.DKGRAY);
            else if (alignment == SettingsProvider.ALIGN_CENTER) alignCenter.setBackgroundColor(Color.DKGRAY);
            else if (alignment == SettingsProvider.ALIGN_RIGHT) alignRight.setBackgroundColor(Color.DKGRAY);
        });
        mViewModel.getTodoOrder().observe(this, order -> {
            orderAsc.setBackgroundColor(Color.TRANSPARENT);
            orderDesc.setBackgroundColor(Color.TRANSPARENT);
            if (order == SettingsProvider.ORDER_ASC) orderAsc.setBackgroundColor(Color.DKGRAY);
            else if (order == SettingsProvider.ORDER_DESC) orderDesc.setBackgroundColor(Color.DKGRAY);
        });
    }

    private void onRateAppClick(View v) {
        Utils.rateAppInGooglePlay(getContext());
    }

    private void onHelpClick(View v) {
        if (mHelpFragment == null) {
            mHelpFragment = new HelpFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.helpContainer, mHelpFragment)
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .remove(mHelpFragment)
                    .commit();
            mHelpFragment = null;
        }
    }
}
