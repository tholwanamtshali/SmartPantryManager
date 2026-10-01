package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.widget.Button;

import android.database.Cursor;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private LinearLayout pantryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        pantryContainer = findViewById(R.id.pantryContainer);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        TextView navRecipes = findViewById(R.id.navRecipes);

        navRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void loadPantry() {

        pantryContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllIngredients();

        if (cursor == null || cursor.getCount() == 0) {

            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No ingredients yet.\nTap Add ingredient to get started.");
            emptyMessage.setTextSize(15);
            emptyMessage.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            emptyMessage.setPadding(20, 40, 20, 40);

            pantryContainer.addView(emptyMessage);

            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)
            );

            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXPIRY_DATE)
            );

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(24, 18, 24, 18);
            card.setBackgroundResource(R.drawable.input_background);

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            cardParams.setMargins(0, 0, 0, 16);
            card.setLayoutParams(cardParams);

            TextView nameText = new TextView(this);
            nameText.setText(name);
            nameText.setTextSize(17);
            nameText.setTypeface(null, Typeface.BOLD);

            TextView quantityText = new TextView(this);
            quantityText.setText(quantity + " " + unit);
            quantityText.setTextSize(14);

            card.addView(nameText);
            card.addView(quantityText);

            if (expiryDate != null && !expiryDate.isEmpty()) {

                TextView expiryText = new TextView(this);
                expiryText.setText("Expiry: " + expiryDate);
                expiryText.setTextSize(13);

                card.addView(expiryText);
            }

            Button editButton = new Button(this);
            editButton.setText("Edit");
            editButton.setAllCaps(false);

            editButton.setOnClickListener(v -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        AddEditIngredientActivity.class
                );

                intent.putExtra("ingredient_id", id);
                intent.putExtra("ingredient_name", name);
                intent.putExtra("ingredient_quantity", quantity);
                intent.putExtra("ingredient_unit", unit);
                intent.putExtra("ingredient_expiry", expiryDate);

                startActivity(intent);
            });

            card.addView(editButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");
            deleteButton.setAllCaps(false);

            deleteButton.setOnClickListener(v -> {

                boolean deleted = databaseHelper.deleteIngredient(id);

                if (deleted) {

                    Toast.makeText(
                            MainActivity.this,
                            "Ingredient deleted",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadPantry();

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Unable to delete ingredient",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            card.addView(deleteButton);

            pantryContainer.addView(card);
        }

        cursor.close();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && pantryContainer != null) {
            loadPantry();
        }
    }
    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}