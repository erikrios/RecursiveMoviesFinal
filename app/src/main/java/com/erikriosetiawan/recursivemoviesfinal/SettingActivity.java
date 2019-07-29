package com.erikriosetiawan.recursivemoviesfinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch switchRelease;
    private Switch switchDaily;

    private boolean daily, release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchRelease = findViewById(R.id.switch_release_reminder);
        switchDaily = findViewById(R.id.switch_daily_reminder);

        SharedPreferences sharedPreferences = getSharedPreferences("com.erikriosetiawan.recursivemoviesfinal", MODE_PRIVATE);
        switchRelease.setChecked(sharedPreferences.getBoolean("Release", false));
        switchDaily.setChecked(sharedPreferences.getBoolean("Daily", false));

        switchRelease.setOnCheckedChangeListener(this);
        switchDaily.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences("com.erikriosetiawan.recursivemoviesfinal", MODE_PRIVATE).edit();

        switch (buttonView.getId()) {
            case R.id.switch_release_reminder:
                release = switchRelease.isChecked();
                editor.putBoolean("Release", release);
                editor.apply();
                break;

            case R.id.switch_daily_reminder:
                daily = switchDaily.isChecked();
                editor.putBoolean("Daily", daily);
                editor.apply();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Release", release);
        outState.putBoolean("Daily", daily);
        super.onSaveInstanceState(outState);
    }
}