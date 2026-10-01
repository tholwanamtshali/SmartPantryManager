package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;

import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout recipesContainer;
    private TextView tvNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        databaseHelper = new DatabaseHelper(this);
        recipesContainer = findViewById(R.id.recipesContainer);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        TextView navPantry = findViewById(R.id.navPantry);
        navPantry.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Map<String, Double> getPantryItems() {

        Map<String, Double> pantry = new HashMap<>();

        Cursor cursor = databaseHelper.getAllIngredients();

        while (cursor.moveToNext()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)
            );

            pantry.put(name.toLowerCase().trim(), quantity);
        }

        cursor.close();

        return pantry;
    }
}