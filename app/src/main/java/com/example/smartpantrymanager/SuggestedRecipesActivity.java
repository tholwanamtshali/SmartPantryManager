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
    private LinearLayout almostThereContainer;
    private LinearLayout almostThereSection;
    private TextView tvNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);
        recipesContainer = findViewById(R.id.recipesContainer);
        almostThereContainer = findViewById(R.id.almostThereContainer);
        almostThereSection = findViewById(R.id.almostThereSection);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);

        LinearLayout navPantry = findViewById(R.id.navPantry);
        navPantry.setOnClickListener(v -> finish());

        LinearLayout navSettings = findViewById(R.id.navSettings);
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

    /**
     * Reads the pantry from the database and returns a map of
     * ingredient name -> quantity already converted into the
     * standard base unit for that ingredient.
     */
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

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)
            );

            String key = name.toLowerCase().trim();

            double normalised = convertToBaseUnit(
                    key, quantity, unit
            );

            // If the same ingredient is stored twice, combine
            if (pantry.containsKey(key)) {
                normalised += pantry.get(key);
            }

            pantry.put(key, normalised);
        }

        cursor.close();

        return pantry;
    }

    private void loadSuggestedRecipes() {

        recipesContainer.removeAllViews();
        almostThereContainer.removeAllViews();

        Map<String, Double> pantry = getPantryItems();

        boolean recipeFound = false;
        boolean almostThereFound = false;

        for (Recipe recipe : RecipeRepository.getRecipes()) {

            int missingCount = countMissingIngredients(recipe, pantry);

            if (missingCount == 0) {

                String details =
                        recipe.getTime() +
                                " • " +
                                recipe.getRequiredIngredients().size() +
                                " ingredients";

                addRecipeCard(
                        recipesContainer,
                        recipe.getTitle(),
                        details,
                        "✓ You have everything.",
                        true
                );

                recipeFound = true;

            } else if (missingCount == 1) {

                String details =
                        recipe.getTime() +
                                " • missing " +
                                getMissingIngredientName(recipe, pantry);

                addRecipeCard(
                        almostThereContainer,
                        recipe.getTitle(),
                        details,
                        "Add the missing ingredient to unlock.",
                        false
                );

                almostThereFound = true;
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

        almostThereSection.setVisibility(
                almostThereFound ? View.VISIBLE : View.GONE
        );
    }

    private int countMissingIngredients(
            Recipe recipe,
            Map<String, Double> pantry
    ) {

        int missing = 0;

        for (Map.Entry<String, Double> requirement :
                recipe.getRequiredIngredients().entrySet()) {

            String ingredient = requirement.getKey();
            double requiredQuantity = requirement.getValue();

            double availableQuantity =
                    getPantryQuantity(pantry, ingredient);

            if (availableQuantity < requiredQuantity) {
                missing++;

                if (missing > 1) {
                    return missing;
                }
            }
        }

        return missing;
    }

    private String getMissingIngredientName(
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
                return formatIngredientName(ingredient);
            }
        }

        return "1 ingredient";
    }

    private String formatIngredientName(String ingredient) {

        if (ingredient == null || ingredient.isEmpty()) {
            return ingredient;
        }

        String lower = ingredient.toLowerCase().trim();

        if (lower.equals("eggs")) {
            return "egg";
        }

        if (lower.equals("tomatoes")) {
            return "tomato";
        }

        return ingredient;
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

    /**
     * Converts a pantry quantity in its stored unit into the
     * standard base unit for that ingredient. The base units
     * match what RecipeRepository uses:
     *
     *   milk, yoghurt  -> millilitres (ml)
     *   cereal         -> grams (g)
     *   everything else -> pieces / slices (count)
     */
    private double convertToBaseUnit(
            String ingredient,
            double quantity,
            String unit
    ) {

        if (unit == null) {
            return quantity;
        }

        String u = unit.toLowerCase().trim();

        // Normalise litre / millilitre
        if (u.equals("l") || u.equals("litre") || u.equals("liter")
                || u.equals("litres") || u.equals("liters")) {
            return quantity * 1000.0;
        }

        if (u.equals("ml") || u.equals("millilitre") || u.equals("milliliter")
                || u.equals("millilitres") || u.equals("milliliters")) {
            return quantity;
        }

        // Normalise kilogram / gram
        if (u.equals("kg") || u.equals("kilogram")
                || u.equals("kilograms")) {
            return quantity * 1000.0;
        }

        if (u.equals("g") || u.equals("gram") || u.equals("grams")) {
            return quantity;
        }

        // Everything else (pieces, slices, cups, etc.) — assume 1:1
        return quantity;
    }

    private void addRecipeCard(
            LinearLayout parent,
            String title,
            String details,
            String availability,
            boolean available
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
                getResources().getColor(
                        available
                                ? R.color.pantry_pink
                                : R.color.pantry_secondary
                )
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

        parent.addView(card);
    }
}