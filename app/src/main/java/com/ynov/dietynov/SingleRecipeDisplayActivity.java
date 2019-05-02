package com.ynov.dietynov;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SingleRecipeDisplayActivity extends AppCompatActivity {

    private Recipe recipe;
    private boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe_display);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        recipe = (Recipe) extras.getSerializable("Recipe");

        isFav = checkIsFav();

        //référence aux views
        ImageView image = findViewById(R.id.IV_recipeImage);
        TextView name = findViewById(R.id.TV_nameRecipe);
        TextView timePrep = findViewById(R.id.TV_timePrep);
        TextView timeBake = findViewById(R.id.TV_timeBake);
        TextView timeTot = findViewById(R.id.TV_timeTotal);
        TextView kcal = findViewById(R.id.TV_kcal_value);
        TextView protein = findViewById(R.id.TV_protein_value);
        TextView fat = findViewById(R.id.TV_fat_value);
        TextView carbohydrate = findViewById(R.id.TV_carbohydrate_value);
        TextView sat_fat = findViewById(R.id.TV_sat_fat_value);
        TextView sugar = findViewById(R.id.TV_sugar_value);
        TextView fiber = findViewById(R.id.TV_fiber_value);
        TextView sodium = findViewById(R.id.TV_sodium_value);

        //référence aux layouts
        LinearLayout ingredientLayout = findViewById(R.id.LO_ingredients);
        LinearLayout stepLayout = findViewById(R.id.LO_steps);

        //paramètres de view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);

        //affectation des valeurs
        Picasso.get().load(recipe.getImageURL()).into(image);
        name.setText(recipe.getName() + " (" + recipe.getPortions() + " portion.s)");
        timePrep.setText("Préparation : " + recipe.getTime().getPrep() + " min");
        timeBake.setText("Cuisson : " + recipe.getTime().getBaking() + " min");
        timeTot.setText("Total : " + recipe.getTime().getTotal() + " min");


        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            TextView tvIngredient = new TextView(this);
            if (ingredient.getUnit().equals("sans unité"))
                ingredient.setUnit("");
            tvIngredient.setText(ingredient.getName() + " : " + ingredient.getQuantity() + " " + ingredient.getUnit());
            tvIngredient.setTextColor(Color.BLACK);
            tvIngredient.setTextSize(20);
            ingredientLayout.addView(tvIngredient);
        }

        for (int i = 0; i < recipe.getSteps().size(); i++) {
            Step step = recipe.getSteps().get(i);
            TextView tvStep = new TextView(this);
            tvStep.setText(step.getOrder() + " : " + step.getTextStep());
            tvStep.setTextColor(Color.BLACK);
            tvStep.setTextSize(20);
            tvStep.setLayoutParams(params);
            stepLayout.addView(tvStep);
        }

        Nutrition nutrition = recipe.getNutrition();

        kcal.setText(Double.toString(nutrition.getKcal()));
        protein.setText(Double.toString(nutrition.getProtein()));
        fat.setText(Double.toString(nutrition.getFat()));
        carbohydrate.setText(Double.toString(nutrition.getCarbohydrate()));
        sat_fat.setText(Double.toString(nutrition.getSat_fat()));
        sugar.setText(Double.toString(nutrition.getSugar()));
        fiber.setText(Double.toString(nutrition.getFiber()));
        sodium.setText(Double.toString(nutrition.getSodium()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipeinfo, menu);
        MenuItem menuFavorite = menu.findItem(R.id.action_favorite);

        if (isFav) {
            menuFavorite.setIcon(R.drawable.ic_favorite);
        } else {
            menuFavorite.setIcon(R.drawable.ic_favorite_off);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            if (isFav) {
                item.setIcon(R.drawable.ic_favorite_off);
                Toast.makeText(this, "Recettes supprimée des favoris", Toast.LENGTH_SHORT).show();
                dbHelper.deleteRecipe(recipe.getId());
                isFav = false;
            } else {
                item.setIcon(R.drawable.ic_favorite);
                Toast.makeText(this, "Recettes ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                dbHelper.insertRecipe(recipe);
                isFav = true;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIsFav() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        boolean isFav = dbHelper.getRecipeIsFav(recipe.getId());

        return isFav;
    }
}
