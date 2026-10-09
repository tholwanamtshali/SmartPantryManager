package com.example.smartpantrymanager;

import java.util.Map;

public class Recipe {

    private final String title;
    private final String time;
    private final String servings;
    private final String icon;
    private final Map<String, Double> requiredIngredients;
    private final String method;

    public Recipe(
            String title,
            String time,
            String servings,
            String icon,
            Map<String, Double> requiredIngredients,
            String method
    ) {
        this.title = title;
        this.time = time;
        this.servings = servings;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public Map<String, Double> getRequiredIngredients() {
        return requiredIngredients;
    }

    public String getMethod() {
        return method;
    }
}