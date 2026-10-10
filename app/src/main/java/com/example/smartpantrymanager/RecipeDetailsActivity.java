package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);

        TextView btnBack = findViewById(R.id.btnBack);
        TextView tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        ImageView ivRecipeBanner = findViewById(R.id.ivRecipeBanner);
        TextView tvRecipeMeta = findViewById(R.id.tvRecipeMeta);
        TextView tvMethod = findViewById(R.id.tvMethod);
        LinearLayout ingredientsContainer =
                findViewById(R.id.ingredientsContainer);

        String recipeTitle = getIntent().getStringExtra("recipe_title");

        ingredientsContainer.removeAllViews();

        Recipe recipe = RecipeRepository.getRecipeByTitle(recipeTitle);

        if (recipe != null) {

            tvRecipeTitle.setText(recipe.getTitle());

            ivRecipeBanner.setImageResource(recipe.getIconRes());

            tvRecipeMeta.setText(
                    recipe.getTime() + " • " + recipe.getServings()
            );

            for (Map.Entry<String, Double> ingredient :
                    recipe.getRequiredIngredients().entrySet()) {

                addIngredientRow(
                        ingredientsContainer,
                        formatIngredientName(ingredient.getKey()),
                        formatIngredientAmount(
                                ingredient.getKey(),
                                ingredient.getValue()
                        )
                );
            }

            tvMethod.setText(recipe.getMethod());
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void addIngredientRow(
            LinearLayout container,
            String ingredient,
            String amount
    ) {
        TextView row = new TextView(this);

        row.setText("✓  " + ingredient + "        " + amount);
        row.setTextSize(15);
        row.setTextColor(getResources().getColor(R.color.pantry_dark));
        row.setPadding(8, 12, 8, 12);

        container.addView(row);
    }

    private String formatIngredientName(String ingredient) {

        if (ingredient.equalsIgnoreCase("eggs")) {
            return "Egg";
        }

        if (ingredient.equalsIgnoreCase("tomatoes")) {
            return "Tomato";
        }

        String firstLetter =
                ingredient.substring(0, 1).toUpperCase();

        return firstLetter + ingredient.substring(1);
    }

    private String formatIngredientAmount(
            String ingredient,
            double quantity
    ) {

        int wholeQuantity = (int) quantity;

        switch (ingredient.toLowerCase()) {

            case "bread":
                return wholeQuantity + " slices";

            case "eggs":
            case "tomatoes":
            case "banana":
                return wholeQuantity + " piece" +
                        (wholeQuantity == 1 ? "" : "s");

            case "milk":
            case "yoghurt":
            case "yogurt":
                return wholeQuantity + " ml";

            case "cereal":
                return wholeQuantity + " g";

            case "cheese":
                return wholeQuantity + " slice" +
                        (wholeQuantity == 1 ? "" : "s");

            default:
                return String.valueOf(quantity);
        }
    }
}