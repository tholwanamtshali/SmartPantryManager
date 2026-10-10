package com.example.smartpantrymanager;

import java.util.Map;

public class Recipe {

    private final String title;
    private final String time;
    private final String servings;
    private final int iconRes;
    private final Map<String, Double> requiredIngredients;
    private final String method;

    public Recipe(
            String title,
            String time,
            String servings,
            int iconRes,
            Map<String, Double> requiredIngredients,
            String method
    ) {
        this.title = title;
        this.time = time;
        this.servings = servings;
        this.iconRes = iconRes;
        this.requiredIngredients = requiredIngredients;
        this.method = method;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getServings() {
        return servings;
    }

    public int getIconRes() {
        return iconRes;
    }

    public Map<String, Double> getRequiredIngredients() {
        return requiredIngredients;
    }

    public String getMethod() {
        return method;
    }
}