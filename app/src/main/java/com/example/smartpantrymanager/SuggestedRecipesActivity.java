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

import java.util.HashMap;
import java.util.Map;

import android.graphics.Typeface;
import android.view.View;

import android.content.Intent;

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
        loadSuggestedRecipes();
        TextView navPantry = findViewById(R.id.navPantry);
        navPantry.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView navSettings = findViewById(R.id.navSettings);

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && recipesContainer != null) {
            loadSuggestedRecipes();
        }
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

    private double getQuantity(Map<String, Double> pantry, String... names) {

        for (String name : names) {
            Double quantity = pantry.get(name.toLowerCase().trim());

            if (quantity != null) {
                return quantity;
            }
        }

        return 0;
    }

    private void loadSuggestedRecipes() {

        recipesContainer.removeAllViews();

        Map<String, Double> pantry = getPantryItems();

        boolean recipeFound = false;

        // French Toast
        double bread = getQuantity(pantry, "bread");
        double eggs = getQuantity(pantry, "egg", "eggs");
        double milk = getQuantity(pantry, "milk");

        if (bread >= 2 && eggs >= 1 && milk >= 60) {

            addRecipeCard(
                    "French toast",
                    "15 min • 3 ingredients",
                    "✓ You have everything."
            );

            recipeFound = true;
        }

        // Boiled Eggs
        if (eggs >= 1) {

            addRecipeCard(
                    "Boiled eggs",
                    "10 min • 1 ingredient",
                    "✓ You have everything."
            );

            recipeFound = true;
        }

        if (!recipeFound) {

            tvNoRecipes.setText(
                    "No recipes match your pantry yet.\n" +
                            "Add more ingredients to see suggestions."
            );

            tvNoRecipes.setVisibility(View.VISIBLE);
            recipesContainer.addView(tvNoRecipes);
        }
    }

    private void addRecipeCard(
            String title,
            String details,
            String availability
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(24, 24, 24, 24);
        card.setBackgroundResource(R.drawable.input_background);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, 18);
        card.setLayoutParams(params);

        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(19);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(
                getResources().getColor(R.color.pantry_dark)
        );

        TextView detailsText = new TextView(this);
        detailsText.setText(details);
        detailsText.setTextSize(14);
        detailsText.setTextColor(
                getResources().getColor(R.color.pantry_secondary)
        );

        TextView availabilityText = new TextView(this);
        availabilityText.setText(availability);
        availabilityText.setTextSize(14);
        availabilityText.setTextColor(
                getResources().getColor(R.color.pantry_pink)
        );

        card.addView(titleText);
        card.addView(detailsText);
        card.addView(availabilityText);

        card.addView(titleText);
        card.addView(detailsText);
        card.addView(availabilityText);

        card.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    RecipeDetailsActivity.class
            );

            intent.putExtra("recipe_title", title);

            startActivity(intent);
        });

        recipesContainer.addView(card);

        recipesContainer.addView(card);
    }
}