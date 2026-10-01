package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        TextView btnBack = findViewById(R.id.btnBack);
        TextView tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvRecipeBanner = findViewById(R.id.tvRecipeBanner);
        TextView tvRecipeMeta = findViewById(R.id.tvRecipeMeta);
        TextView tvMethod = findViewById(R.id.tvMethod);
        LinearLayout ingredientsContainer = findViewById(R.id.ingredientsContainer);

        String recipeTitle = getIntent().getStringExtra("recipe_title");

        ingredientsContainer.removeAllViews();

        if ("Boiled eggs".equalsIgnoreCase(recipeTitle)) {

            tvRecipeTitle.setText("Boiled eggs");
            tvRecipeBanner.setText("🥚");
            tvRecipeMeta.setText("10 min • 1 serving");

            addIngredientRow(
                    ingredientsContainer,
                    "Egg",
                    "1 piece"
            );

            tvMethod.setText(
                    "①  Place the egg in a pot and cover with water.\n\n" +
                            "②  Bring the water to a boil.\n\n" +
                            "③  Boil until cooked, then cool and peel."
            );

        } else {

            // Default to French Toast
            tvRecipeTitle.setText("French toast");
            tvRecipeBanner.setText("🍞");
            tvRecipeMeta.setText("15 min • 1 serving");

            addIngredientRow(
                    ingredientsContainer,
                    "Bread",
                    "2 slices"
            );

            addIngredientRow(
                    ingredientsContainer,
                    "Egg",
                    "1 piece"
            );

            addIngredientRow(
                    ingredientsContainer,
                    "Milk",
                    "60 ml"
            );

            tvMethod.setText(
                    "①  Whisk the egg and milk.\n\n" +
                            "②  Dip the bread into the mixture.\n\n" +
                            "③  Cook in a non-stick pan until golden."
            );
        }

        btnBack.setOnClickListener(v -> finish());

        ingredientsContainer.removeAllViews();

        if ("Boiled eggs".equalsIgnoreCase(recipeTitle)) {

            tvRecipeTitle.setText("Boiled eggs");
            tvRecipeBanner.setText("🥚");
            tvRecipeMeta.setText("10 min • 1 serving");

            addIngredientRow(
                    ingredientsContainer,
                    "Egg",
                    "1 piece"
            );

            tvMethod.setText(
                    "①  Place the egg in a saucepan and cover with water.\n\n" +
                            "②  Bring the water to a boil.\n\n" +
                            "③  Cook until the egg reaches your preferred firmness.\n\n" +
                            "④  Cool, peel and serve."
            );

        } else {

            // Default to French Toast
            tvRecipeTitle.setText("French toast");
            tvRecipeBanner.setText("🍞");
            tvRecipeMeta.setText("15 min • 1 serving");

            addIngredientRow(
                    ingredientsContainer,
                    "Bread",
                    "2 slices"
            );

            addIngredientRow(
                    ingredientsContainer,
                    "Egg",
                    "1 piece"
            );

            addIngredientRow(
                    ingredientsContainer,
                    "Milk",
                    "60 ml"
            );

            tvMethod.setText(
                    "①  Whisk the egg and milk.\n\n" +
                            "②  Dip the bread into the mixture.\n\n" +
                            "③  Cook in a non-stick pan until golden."
            );
        }

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
}