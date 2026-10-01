package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.widget.TextView;

import android.content.SharedPreferences;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        TextView navPantry = findViewById(R.id.navPantry);
        TextView navRecipes = findViewById(R.id.navRecipes);

        navPantry.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SettingsActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });

        navRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SettingsActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
            finish();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Switch switchExpiryReminders = findViewById(R.id.switchExpiryReminders);

        SharedPreferences preferences =
                getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        boolean remindersEnabled =
                preferences.getBoolean("expiry_reminders", true);

        switchExpiryReminders.setChecked(remindersEnabled);

        switchExpiryReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
                    .putBoolean("expiry_reminders", isChecked)
                    .apply();
        });

        TextView tvPreferredUnits = findViewById(R.id.tvPreferredUnits);
        View rowPreferredUnits = findViewById(R.id.rowPreferredUnits);

        String savedUnit =
                preferences.getString("preferred_units", "Metric");

        tvPreferredUnits.setText(savedUnit + "  ›");

        rowPreferredUnits.setOnClickListener(v -> {

            String[] options = {"Metric", "Imperial"};

            int checkedItem =
                    "Imperial".equals(savedUnit) ? 1 : 0;

            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Preferred units")
                    .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {

                        String selectedUnit = options[which];

                        preferences.edit()
                                .putString("preferred_units", selectedUnit)
                                .apply();

                        tvPreferredUnits.setText(selectedUnit + "  ›");

                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        View rowAbout = findViewById(R.id.rowAbout);

        rowAbout.setOnClickListener(v -> {

            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("About Smart Pantry")
                    .setMessage(
                            "Smart Pantry Manager helps you keep track of pantry ingredients, " +
                                    "quantities and expiry dates, and suggests recipes based on the food " +
                                    "you already have.\n\n" +
                                    "Version 1.0"
                    )
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}