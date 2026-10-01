package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.TextView;

public class AddEditIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);
        EditText etExpiryDate = findViewById(R.id.etExpiryDate);

        TextView btnBack = findViewById(R.id.btnBack);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        etExpiryDate.setOnClickListener(v -> {

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

                        // Keep database-friendly version for later
                        etExpiryDate.setTag(databaseFormat.format(selectedDate.getTime()));
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.show();
        });
        EditText etIngredientName = findViewById(R.id.etIngredientName);
        EditText etQuantity = findViewById(R.id.etQuantity);
        Spinner spUnit = findViewById(R.id.spUnit);
        Button btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        btnSaveIngredient.setOnClickListener(v -> {

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

            boolean inserted = databaseHelper.addIngredient(
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            if (inserted) {
                Toast.makeText(
                        AddEditIngredientActivity.this,
                        "Ingredient saved",
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
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}