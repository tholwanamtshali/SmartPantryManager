package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spUnit;
    private Button btnSaveIngredient;

    private DatabaseHelper databaseHelper;

    // -1 means "add mode"; any other value means "edit this ingredient id"
    private int editingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper = new DatabaseHelper(this);

        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spUnit = findViewById(R.id.spUnit);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        TextView btnBack = findViewById(R.id.btnBack);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        // ---- Populate fields if we're editing ----
        if (getIntent().hasExtra("ingredient_id")) {

            editingId = getIntent().getIntExtra("ingredient_id", -1);

            String name = getIntent().getStringExtra("ingredient_name");
            double quantity = getIntent().getDoubleExtra("ingredient_quantity", 0);
            String unit = getIntent().getStringExtra("ingredient_unit");
            String expiry = getIntent().getStringExtra("ingredient_expiry");

            if (name != null) {
                etIngredientName.setText(name);
            }

            etQuantity.setText(String.valueOf(quantity));

            if (unit != null) {
                selectSpinnerItem(spUnit, unit);
            }

            if (expiry != null && !expiry.isEmpty()) {
                etExpiryDate.setText(expiry);

                // Store the DB-friendly format in the tag so save works
                etExpiryDate.setTag(expiry);
            }

            btnSaveIngredient.setText("Update ingredient");
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddEditIngredientActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(
                            selectedYear,
                            selectedMonth,
                            selectedDay
                    );

                    SimpleDateFormat displayFormat =
                            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

                    SimpleDateFormat databaseFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    etExpiryDate.setText(displayFormat.format(selectedDate.getTime()));
                    etExpiryDate.setTag(databaseFormat.format(selectedDate.getTime()));
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveIngredient() {

        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spUnit.getSelectedItem().toString();

        String expiryDate = "";

        if (etExpiryDate.getTag() != null) {
            expiryDate = etExpiryDate.getTag().toString();
        }

        if (name.isEmpty()) {
            etIngredientName.setError("Enter an ingredient name");
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter a quantity");
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid quantity");
            return;
        }

        boolean success;

        if (editingId == -1) {
            // Add mode
            success = databaseHelper.addIngredient(
                    name, quantity, unit, expiryDate
            );
        } else {
            // Edit mode
            success = databaseHelper.updateIngredient(
                    editingId, name, quantity, unit, expiryDate
            );
        }

        if (success) {
            Toast.makeText(
                    AddEditIngredientActivity.this,
                    editingId == -1 ? "Ingredient saved" : "Ingredient updated",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {
            Toast.makeText(
                    AddEditIngredientActivity.this,
                    "Unable to save ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * Selects the given value in the unit spinner, case-insensitively.
     * If the value isn't found, the spinner stays on its current selection.
     */
    private void selectSpinnerItem(Spinner spinner, String value) {

        if (spinner == null || value == null) {
            return;
        }

        for (int i = 0; i < spinner.getCount(); i++) {
            Object item = spinner.getItemAtPosition(i);

            if (item != null &&
                    item.toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }
}