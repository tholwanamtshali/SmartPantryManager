package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        TextView navSettings = findViewById(R.id.navSettings);
        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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

        for (Recipe recipe : RecipeRepository.getRecipes()) {

            if (canMakeRecipe(recipe, pantry)) {

                String details =
                        recipe.getTime() +
                                " • " +
                                recipe.getRequiredIngredients().size() +
                                " ingredients";

                addRecipeCard(
                        recipe.getTitle(),
                        details,
                        "✓ You have everything."
                );

                recipeFound = true;
            }
        }

        if (!recipeFound) {

            tvNoRecipes.setText(
                    "No recipes match your pantry yet.\n" +
                            "Add more ingredients to see suggestions."
            );

            tvNoRecipes.setVisibility(View.VISIBLE);

        } else {

            tvNoRecipes.setVisibility(View.GONE);
        }
    }

    private boolean canMakeRecipe(
            Recipe recipe,
            Map<String, Double> pantry
    ) {

        for (Map.Entry<String, Double> requirement :
                recipe.getRequiredIngredients().entrySet()) {

            String ingredient = requirement.getKey();
            double requiredQuantity = requirement.getValue();

            double availableQuantity =
                    getPantryQuantity(pantry, ingredient);

            if (availableQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private double getPantryQuantity(
            Map<String, Double> pantry,
            String ingredient
    ) {

        String name = ingredient.toLowerCase().trim();

        if (pantry.containsKey(name)) {
            return pantry.get(name);
        }

        if (name.equals("eggs") && pantry.containsKey("egg")) {
            return pantry.get("egg");
        }

        if (name.equals("egg") && pantry.containsKey("eggs")) {
            return pantry.get("eggs");
        }

        if (name.equals("tomatoes") && pantry.containsKey("tomato")) {
            return pantry.get("tomato");
        }

        if (name.equals("tomato") && pantry.containsKey("tomatoes")) {
            return pantry.get("tomatoes");
        }

        if (name.equals("yoghurt") && pantry.containsKey("yogurt")) {
            return pantry.get("yogurt");
        }

        if (name.equals("yogurt") && pantry.containsKey("yoghurt")) {
            return pantry.get("yoghurt");
        }

        return 0;
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

        card.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    RecipeDetailsActivity.class
            );

            intent.putExtra("recipe_title", title);

            startActivity(intent);
        });

        recipesContainer.addView(card);
    }
}