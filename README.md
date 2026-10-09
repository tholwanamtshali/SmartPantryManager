##Smart Pantry Manager
A Java Android application that helps users reduce food waste by tracking the
ingredients they already have at home and suggesting recipes they can cook
using strictly those ingredients, no shopping trip required.

##Core feature: Strict Matching
A recipe only appears in the "Suggested Recipes" list if every ingredient
it requires is already in the user's pantry, in at least the required
quantity. Partial matches ("almost there" recipes) are excluded from the
main suggestions.

##Features
- Pantry management: add, edit, and delete ingredients (name, quantity,
  unit, expiry date)
- Pantry list screen using a RecyclerView with a custom `PantryAdapter`
- 15 pre-loaded recipes, each with a title, icon, prep time, servings,
  ingredients, and method
- Suggested Recipes screen running strict matching against the current pantry
- Recipe Detail screen displaying the full ingredient list and method
- Settings screen with an expiring-soon toggle and preferred units
  (persisted using `SharedPreferences`)
- Empty-state feedback when no recipes match

##Database choice: SQLite
I chose **SQLite** (via `SQLiteOpenHelper`) for this application because:

- It is built into Android, so no network, server, or account is required.
- It is the storage approach taught in this module (persistent data chapter).
- Pantry data is entirely local and personal — there is no benefit to a
  cloud backend for a single-user on-device app.
- The full CRUD cycle (Create, Read, Update, Delete) is straightforward to
  implement with `ContentValues`, `Cursor`, and `SQLiteDatabase`.

Data persists between app sessions: closing and reopening the app retains
all pantry items.

## Project structure
- `MainActivity` — Pantry list (RecyclerView)
- `AddEditIngredientActivity` — Add / Edit form (shared for both operations)
- `SuggestedRecipesActivity` — Strict-matching recipe suggestions
- `RecipeDetailsActivity` — Recipe detail view (data-driven)
- `SettingsActivity` — App preferences
- `DatabaseHelper` — SQLite schema and CRUD methods
- `PantryAdapter` — RecyclerView adapter for pantry items
- `RecipeRepository` — In-memory store of the 15 pre-loaded recipes
- `Ingredient`, `Recipe` — model classes

## Setup and run instructions
1. Install **Android Studio** (Koala or newer recommended).
2. Clone this repository: https://github.com/tholwanamtshali/SmartPantryManager
3. Open the project folder in Android Studio.
4. Let Gradle sync finish (first sync may take a few minutes).
5. Connect an Android device (API 24+) with USB debugging enabled, or start
an emulator.
6. Press "Run" to install and launch the app.

The database is created automatically on first launch — no manual setup
required.

## Requirements
- Android Studio (latest stable)
- Java 8+ (bundled with Android Studio)
- Android device or emulator running API 24 or higher

## Author
Palesa Tholwana Mtshali — Mobile App Development 700 Assignment
