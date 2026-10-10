package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRepository {

    public static List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        // 1. French Toast
        Map<String, Double> frenchToast = new HashMap<>();
        frenchToast.put("bread", 2.0);
        frenchToast.put("eggs", 1.0);
        frenchToast.put("milk", 60.0);

        recipes.add(new Recipe(
                "French toast",
                "15 min",
                "1 serving",
                R.drawable.ic_food_default,
                frenchToast,
                "Whisk the egg and milk.\n\n" +
                        "Dip the bread into the mixture.\n\n" +
                        "Cook in a non-stick pan until golden."
        ));

        // 2. Boiled Eggs
        Map<String, Double> boiledEggs = new HashMap<>();
        boiledEggs.put("eggs", 1.0);

        recipes.add(new Recipe(
                "Boiled eggs",
                "10 min",
                "1 serving",
                R.drawable.ic_food_default,
                boiledEggs,
                "Place the egg in a pot and cover with water.\n\n" +
                        "Bring the water to a boil.\n\n" +
                        "Boil until cooked, then cool and peel."
        ));

        // 3. Scrambled Eggs
        Map<String, Double> scrambledEggs = new HashMap<>();
        scrambledEggs.put("eggs", 2.0);
        scrambledEggs.put("milk", 30.0);

        recipes.add(new Recipe(
                "Scrambled eggs",
                "10 min",
                "1 serving",
                R.drawable.ic_food_default,
                scrambledEggs,
                "Whisk the eggs and milk.\n\n" +
                        "Pour into a heated pan.\n\n" +
                        "Stir gently until cooked."
        ));

        // 4. Egg Sandwich
        Map<String, Double> eggSandwich = new HashMap<>();
        eggSandwich.put("bread", 2.0);
        eggSandwich.put("eggs", 1.0);

        recipes.add(new Recipe(
                "Egg sandwich",
                "12 min",
                "1 serving",
                R.drawable.ic_food_default,
                eggSandwich,
                "Cook the egg to your preference.\n\n" +
                        "Place it between two slices of bread.\n\n" +
                        "Serve immediately."
        ));

        // 5. Tomato Toast
        Map<String, Double> tomatoToast = new HashMap<>();
        tomatoToast.put("bread", 2.0);
        tomatoToast.put("tomatoes", 1.0);

        recipes.add(new Recipe(
                "Tomato toast",
                "10 min",
                "1 serving",
                R.drawable.ic_food_default,
                tomatoToast,
                "Toast the bread.\n\n" +
                        "Slice the tomato.\n\n" +
                        "Place the tomato slices onto the toast."
        ));

        // 6. Cheese Toast
        Map<String, Double> cheeseToast = new HashMap<>();
        cheeseToast.put("bread", 2.0);
        cheeseToast.put("cheese", 2.0);

        recipes.add(new Recipe(
                "Cheese toast",
                "10 min",
                "1 serving",
                R.drawable.ic_food_default,
                cheeseToast,
                "Place cheese on the bread.\n\n" +
                        "Toast until the cheese melts.\n\n" +
                        "Serve warm."
        ));

        // 7. Cheese Omelette
        Map<String, Double> cheeseOmelette = new HashMap<>();
        cheeseOmelette.put("eggs", 2.0);
        cheeseOmelette.put("cheese", 1.0);

        recipes.add(new Recipe(
                "Cheese omelette",
                "12 min",
                "1 serving",
                R.drawable.ic_food_default,
                cheeseOmelette,
                "Beat the eggs.\n\n" +
                        "Pour into a heated pan.\n\n" +
                        "Add cheese, fold and cook until ready."
        ));

        // 8. Tomato Omelette
        Map<String, Double> tomatoOmelette = new HashMap<>();
        tomatoOmelette.put("eggs", 2.0);
        tomatoOmelette.put("tomatoes", 1.0);

        recipes.add(new Recipe(
                "Tomato omelette",
                "12 min",
                "1 serving",
                R.drawable.ic_food_default,
                tomatoOmelette,
                "Beat the eggs.\n\n" +
                        "Add chopped tomato.\n\n" +
                        "Cook in a pan until set."
        ));

        // 9. Cereal with Milk
        Map<String, Double> cerealMilk = new HashMap<>();
        cerealMilk.put("cereal", 50.0);
        cerealMilk.put("milk", 200.0);

        recipes.add(new Recipe(
                "Cereal with milk",
                "5 min",
                "1 serving",
                R.drawable.ic_food_default,
                cerealMilk,
                "Add cereal to a bowl.\n\n" +
                        "Pour milk over the cereal.\n\n" +
                        "Serve immediately."
        ));

        // 10. Banana Cereal
        Map<String, Double> bananaCereal = new HashMap<>();
        bananaCereal.put("cereal", 50.0);
        bananaCereal.put("milk", 200.0);
        bananaCereal.put("banana", 1.0);

        recipes.add(new Recipe(
                "Banana cereal",
                "5 min",
                "1 serving",
                R.drawable.ic_food_default,
                bananaCereal,
                "Add cereal and milk to a bowl.\n\n" +
                        "Slice the banana.\n\n" +
                        "Add the banana slices on top."
        ));

        // 11. Banana Toast
        Map<String, Double> bananaToast = new HashMap<>();
        bananaToast.put("bread", 2.0);
        bananaToast.put("banana", 1.0);

        recipes.add(new Recipe(
                "Banana toast",
                "7 min",
                "1 serving",
                R.drawable.ic_food_default,
                bananaToast,
                "Toast the bread.\n\n" +
                        "Slice or mash the banana.\n\n" +
                        "Spread the banana over the toast."
        ));

        // 12. Yoghurt and Banana
        Map<String, Double> yoghurtBanana = new HashMap<>();
        yoghurtBanana.put("yoghurt", 150.0);
        yoghurtBanana.put("banana", 1.0);

        recipes.add(new Recipe(
                "Yoghurt and banana",
                "5 min",
                "1 serving",
                R.drawable.ic_food_default,
                yoghurtBanana,
                "Add yoghurt to a bowl.\n\n" +
                        "Slice the banana.\n\n" +
                        "Add the banana to the yoghurt."
        ));

        // 13. Tomato and Cheese Sandwich
        Map<String, Double> tomatoCheeseSandwich = new HashMap<>();
        tomatoCheeseSandwich.put("bread", 2.0);
        tomatoCheeseSandwich.put("tomatoes", 1.0);
        tomatoCheeseSandwich.put("cheese", 1.0);

        recipes.add(new Recipe(
                "Tomato and cheese sandwich",
                "8 min",
                "1 serving",
                R.drawable.ic_food_default,
                tomatoCheeseSandwich,
                "Slice the tomato and cheese.\n\n" +
                        "Place them between two slices of bread.\n\n" +
                        "Serve fresh or toasted."
        ));

        // 14. Egg and Tomato Sandwich
        Map<String, Double> eggTomatoSandwich = new HashMap<>();
        eggTomatoSandwich.put("bread", 2.0);
        eggTomatoSandwich.put("eggs", 1.0);
        eggTomatoSandwich.put("tomatoes", 1.0);

        recipes.add(new Recipe(
                "Egg and tomato sandwich",
                "15 min",
                "1 serving",
                R.drawable.ic_food_default,
                eggTomatoSandwich,
                "Cook the egg.\n\n" +
                        "Slice the tomato.\n\n" +
                        "Place both between two slices of bread."
        ));

        // 15. Egg and Cheese Sandwich
        Map<String, Double> eggCheeseSandwich = new HashMap<>();
        eggCheeseSandwich.put("bread", 2.0);
        eggCheeseSandwich.put("eggs", 1.0);
        eggCheeseSandwich.put("cheese", 1.0);

        recipes.add(new Recipe(
                "Egg and cheese sandwich",
                "15 min",
                "1 serving",
                R.drawable.ic_food_default,
                eggCheeseSandwich,
                "Cook the egg.\n\n" +
                        "Place the egg and cheese on the bread.\n\n" +
                        "Close the sandwich and serve."
        ));

        return recipes;
    }

    public static Recipe getRecipeByTitle(String title) {

        for (Recipe recipe : getRecipes()) {

            if (recipe.getTitle().equalsIgnoreCase(title)) {
                return recipe;
            }
        }

        return null;
    }
}