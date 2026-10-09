package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements PantryAdapter.OnIngredientActionListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView rvPantry;
    private TextView tvEmptyPantry;
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        rvPantry = findViewById(R.id.rvPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);

        pantryAdapter = new PantryAdapter(this);

        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        rvPantry.setAdapter(pantryAdapter);

        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );
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

        TextView navSettings = findViewById(R.id.navSettings);
        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
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

        if (databaseHelper != null) {
            loadPantry();
        }
    }

    private void loadPantry() {

        List<Ingredient> ingredients =
                databaseHelper.getAllIngredientsList();

        pantryAdapter.setIngredients(ingredients);

        if (ingredients.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            rvPantry.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEdit(Ingredient ingredient) {

        Intent intent = new Intent(
                MainActivity.this,
                AddEditIngredientActivity.class
        );

        intent.putExtra("ingredient_id", ingredient.getId());
        intent.putExtra("ingredient_name", ingredient.getName());
        intent.putExtra("ingredient_quantity", ingredient.getQuantity());
        intent.putExtra("ingredient_unit", ingredient.getUnit());
        intent.putExtra("ingredient_expiry", ingredient.getExpiryDate());

        startActivity(intent);
    }

    @Override
    public void onDelete(Ingredient ingredient) {

        boolean deleted = databaseHelper.deleteIngredient(
                ingredient.getId()
        );

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
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}