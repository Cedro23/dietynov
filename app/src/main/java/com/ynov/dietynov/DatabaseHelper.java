package com.ynov.dietynov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "android_dietynov.db";

    private static final String MEASUREMENTS_TABLE = "measurements_table";
    private static final String RECIPES_TABLE = "recipes_table";
    private static final String INGREDIENTS_TABLE = "ingregients_table";
    private static final String STEPS_TABLE = "steps_table";

    //Champs all tables
    private static final String _ID = "id";

    //Champs measurements_table
    private static final String _TYPE = "type";
    private static final String _DATE = "date";
    private static final String _VALUE = "value";

    //Champs recipes_table ingredients_table steps_table
    private static final String _ID_RECETTE = "id_recipe";

    //Champs recipes_table
    private static final String _NAME_RECIPE = "name";
    private static final String _IMG_URL = "img_url";
    private static final String _PORTIONS = "portions";
    private static final String _TIME_PREP = "time_prep";
    private static final String _TIME_BAKE = "time_bake";
    private static final String _TIME_TOT = "time_tot";
    private static final String _KCAL = "kcal";
    private static final String _PROTEIN = "protein";
    private static final String _FAT = "fat";
    private static final String _CARBOHYDRATE = "carbohydrate";
    private static final String _SAT_FAT = "sat_fat";
    private static final String _SUGAR = "sugar";
    private static final String _FIBER = "fiber";
    private static final String _SODIUM = "sodium";

    //Champs ingredients_table
    private static final String _NAME_INGREDIENT = "name";
    private static final String _QUANTITY = "quantity";
    private static final String _UNIT = "unit";

    //Champs steps_table
    private static final String _STEP_NUMBER = "step_number";
    private static final String _STEP = "step";


    public DatabaseHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = _context;
    }

    private static final String CREATE_TABLE_MEASUREMENTS_TBL = "CREATE TABLE " + MEASUREMENTS_TABLE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + _TYPE + " VARCHAR(10) NOT NULL ," + _DATE + " INTEGER(8) NOT NULL, " + _VALUE + " REAL NOT NULL)";
    private static final String CREATE_TABLE_RECIPES_TBL = "CREATE TABLE " + RECIPES_TABLE + "(" + _ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " + _ID_RECETTE + " INTEGER NOT NULL, " + _NAME_RECIPE + " VARCHAR NOT NULL, " + _IMG_URL + " VARCHAR NOT NULL, " + _PORTIONS + " INTEGER NOT NULL, " + _TIME_PREP + " INTEGER NOT NULL, " + _TIME_BAKE + " INTEGER NOT NULL, " + _TIME_TOT + " INTEGER NOT NULL, " + _KCAL + " REAL NOT NULL, " + _PROTEIN + " REAL NOT NULL, " + _FAT + " REAL NOT NULL, " + _CARBOHYDRATE + " REAL NOT NULL, " + _SAT_FAT + " REAL NOT NULL, " + _SUGAR + " REAL NOT NULL, " + _FIBER + " REAL NOT NULL, " + _SODIUM + " REAL NOT NULL)";
    private static final String CREATE_TABLE_INGREDIENTS_TBL = "CREATE TABLE " + INGREDIENTS_TABLE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + _ID_RECETTE + " INTEGER NOT NULL, " + _NAME_INGREDIENT + " VARCHAR NOT NULL, " + _QUANTITY + " VARCHAR NOT NULL, " + _UNIT + " VARCHAR NOT NULL)";
    private static final String CREATE_TABLE_STEPS_TBL = "CREATE TABLE " + STEPS_TABLE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + _ID_RECETTE + " INTEGER NOT NULL, " + _STEP_NUMBER + " INTEGER NOT NULL, " + _STEP + " VARCHAR NOT NULL)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEASUREMENTS_TBL);
        db.execSQL(CREATE_TABLE_RECIPES_TBL);
        db.execSQL(CREATE_TABLE_INGREDIENTS_TBL);
        db.execSQL(CREATE_TABLE_STEPS_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INGREDIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STEPS_TABLE);

        onCreate(db);
    }

    //insert data in measurements table
    public void insertDataInMeasurements(String _type, long _date, float _value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(_TYPE, _type); //Add type
        cv.put(_DATE, _date); //Add date
        cv.put(_VALUE, _value); //Add value

        db.insert(MEASUREMENTS_TABLE, null, cv);
        db.close();
    }

    //fetch all of one type from measurements
    public ArrayList<MeasurementData> fetchAllOfOneTypeFromMeasurements(String _type) {
        ArrayList<MeasurementData> listMeasurementData = new ArrayList<>();

        String selectQuery = "SELECT  date, value FROM " + MEASUREMENTS_TABLE + " WHERE type = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, new String[]{_type}, null);

        if (c.moveToFirst()) {
            do {
                listMeasurementData.add(new MeasurementData(c.getInt(0), c.getFloat(1)));
            } while (c.moveToNext());
        }

        c.close();

        return listMeasurementData;
    }

    //delete all of one type from measurements
    public void deleteDataFromOneInMeasurements(String _type) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(MEASUREMENTS_TABLE, "type = ?", new String[]{_type});
        db.close();
    }


    //insert data in measurements table
    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Partie recettes
        ContentValues cvRecipe = new ContentValues();
        cvRecipe.put(_ID_RECETTE, recipe.getId());
        cvRecipe.put(_NAME_RECIPE, recipe.getName());
        cvRecipe.put(_IMG_URL, recipe.getImageURL());
        cvRecipe.put(_PORTIONS, recipe.getPortions());
        cvRecipe.put(_TIME_PREP, recipe.getTime().getPrep());
        cvRecipe.put(_TIME_BAKE, recipe.getTime().getBaking());
        cvRecipe.put(_TIME_TOT, recipe.getTime().getTotal());
        cvRecipe.put(_KCAL, recipe.getNutrition().getKcal());
        cvRecipe.put(_PROTEIN, recipe.getNutrition().getProtein());
        cvRecipe.put(_FAT, recipe.getNutrition().getFat());
        cvRecipe.put(_CARBOHYDRATE, recipe.getNutrition().getCarbohydrate());
        cvRecipe.put(_SAT_FAT, recipe.getNutrition().getSat_fat());
        cvRecipe.put(_SUGAR, recipe.getNutrition().getSugar());
        cvRecipe.put(_FIBER, recipe.getNutrition().getFiber());
        cvRecipe.put(_SODIUM, recipe.getNutrition().getSodium());

        db.insert(RECIPES_TABLE, null, cvRecipe);

        //Partie ingrédients
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ContentValues cvIngredients = new ContentValues();
            cvIngredients.put(_ID_RECETTE, recipe.getId());
            cvIngredients.put(_NAME_INGREDIENT, ingredient.getName());
            cvIngredients.put(_QUANTITY, ingredient.getQuantity());
            cvIngredients.put(_UNIT, ingredient.getUnit());

            db.insert(INGREDIENTS_TABLE, null, cvIngredients);
        }

        //Partie étapes
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            Step steps = recipe.getSteps().get(i);
            ContentValues cvSteps = new ContentValues();
            cvSteps.put(_ID_RECETTE, recipe.getId());
            cvSteps.put(_STEP_NUMBER, steps.getOrder());
            cvSteps.put(_STEP, steps.getTextStep());

            db.insert(STEPS_TABLE, null, cvSteps);
        }

        db.close();
    }

    //delete recipe
    public void deleteRecipe(int id_recipe) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(RECIPES_TABLE, "id_recipe = " + id_recipe, null);
        db.delete(INGREDIENTS_TABLE, "id_recipe = " + id_recipe, null);
        db.delete(STEPS_TABLE, "id_recipe = " + id_recipe, null);
        db.close();
    }


    //Fetch all recipes from Recipe table
    public ArrayList<Recipe> fetchAllFromRecipe() {
        ArrayList<Recipe> listRecipes = new ArrayList<>();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Step> steps = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + RECIPES_TABLE;
        Cursor c = db.rawQuery(selectQuery, null, null);

        String selectQueryIngredient = "SELECT  * FROM " + INGREDIENTS_TABLE;
        Cursor c2 = db.rawQuery(selectQueryIngredient, null, null);

        String selectQueryStep = "SELECT  * FROM " + STEPS_TABLE;
        Cursor c3 = db.rawQuery(selectQueryStep, null, null);

        if (c.moveToFirst()) {
            Timing timing = new Timing(c.getInt(5), c.getInt(6), c.getInt(7));

            if (c2.moveToFirst()) {
                do {
                    Ingredient ingredient = new Ingredient(c2.getDouble(3), c2.getString(4), c2.getString(2));
                    ingredients.add(ingredient);
                } while (c2.moveToNext());
            }

            if (c3.moveToFirst()) {
                do {
                    Step step = new Step(c3.getInt(2), c3.getString(3));
                    steps.add(step);
                } while (c3.moveToNext());
            }

            Nutrition nutrition = new Nutrition(c.getDouble(8), c.getDouble(9), c.getDouble(10), c.getDouble(11), c.getDouble(12), c.getDouble(13), c.getDouble(14), c.getDouble(15));
            Recipe recipe = new Recipe(c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), timing, ingredients, steps, nutrition);
            listRecipes.add(recipe);
        }

        return listRecipes;
    }

    //Vérifie combien de fois la recette se trouve dans la BD
    public boolean getRecipeIsFav(int _idRecipe) {
        String selectQuery = "SELECT  * FROM " + RECIPES_TABLE + " WHERE id_recipe = " + _idRecipe;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null, null);

        //Si la recette n'est pas présente dans la BD, alors on renvoie "false"
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        //Si la recette est présente dans la BD au moins une fois (max = 1), alors on revoie "true"
        else {
            cursor.close();
            return true;
        }
    }
}
