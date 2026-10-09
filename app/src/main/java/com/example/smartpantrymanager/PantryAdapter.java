package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnIngredientActionListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    // Full list — everything in the pantry
    private final List<Ingredient> allIngredients = new ArrayList<>();

    // Currently displayed list — equals allIngredients when no filter is active
    private final List<Ingredient> visibleIngredients = new ArrayList<>();

    private final OnIngredientActionListener listener;

    public PantryAdapter(OnIngredientActionListener listener) {
        this.listener = listener;
    }

    /**
     * Replace the entire pantry list (called after loading from the DB).
     * Also re-applies the current filter if one is set.
     */
    public void setIngredients(List<Ingredient> newList) {
        allIngredients.clear();
        allIngredients.addAll(newList);
        applyFilter(currentQuery);
    }

    private String currentQuery = "";

    /**
     * Filters the pantry by ingredient name, case-insensitively.
     * Passing null or "" clears the filter and shows everything.
     */
    public void filter(String query) {
        currentQuery = query == null ? "" : query;
        applyFilter(currentQuery);
    }

    private void applyFilter(String query) {

        visibleIngredients.clear();

        String q = query.toLowerCase(Locale.getDefault()).trim();

        if (q.isEmpty()) {
            visibleIngredients.addAll(allIngredients);
        } else {
            for (Ingredient ingredient : allIngredients) {
                if (ingredient.getName()
                        .toLowerCase(Locale.getDefault())
                        .contains(q)) {
                    visibleIngredients.add(ingredient);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position
    ) {
        Ingredient ingredient = visibleIngredients.get(position);

        holder.tvName.setText(ingredient.getName());

        holder.tvQuantity.setText(
                ingredient.getQuantity() + " " + ingredient.getUnit()
        );

        String expiry = ingredient.getExpiryDate();

        if (expiry != null && !expiry.isEmpty()) {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setText("Expiry: " + expiry);
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(ingredient);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visibleIngredients.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvQuantity;
        TextView tvExpiry;
        Button btnEdit;
        Button btnDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
            tvExpiry = itemView.findViewById(R.id.tvIngredientExpiry);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}