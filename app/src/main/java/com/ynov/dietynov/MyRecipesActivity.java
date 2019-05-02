package com.ynov.dietynov;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MyRecipesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Recipe> listRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.getString("fetchType").equals("database")){
                //code when needing list from DB
                listRecipes = fetchListFromDB();
            }

            if (extras.getString("fetchType").equals("webservice")){
                //code when needing list from WebService
                instantiateRequestQueue();
            }
        }
        else {
            instantiateRequestQueue();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myrecipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            Toast.makeText(this, "Liste triée", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_recipes)
        {
            Intent intent = new Intent();

            if (id == R.id.nav_home) {
                // Handle the home action
                intent = new Intent(this, MainActivity.class);
            } else if (id == R.id.nav_account) {
                // Handle the account action
                intent = new Intent(this, MyAccountActivity.class);
            }
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //fetching recipe list from database
    private ArrayList<Recipe> fetchListFromDB(){
        ArrayList<Recipe> recipesList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);

//        recipesList = dbHelper.fetchAllFromRecipe();
        return recipesList;
    }

    private void displayRecyclerView() {
        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;

        //Partie RecyclerView
        recyclerView = findViewById(R.id.RV_recipes);

        //Ajout de la ligne de division
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        //Instanciation de LinearLayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Instanciation de l'adapter
        mAdapter = new RVAdapterRecipes(this, listRecipes);
        recyclerView.setAdapter(mAdapter);
    }

    private void instantiateRequestQueue() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://cedric-lesueur.com/json/flux_recettes.json";

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseObject) {
                listRecipes = new ArrayList<>();
                JSONObject jsonObject = responseObject;
                try{
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject currentRecipe = jsonArray.getJSONObject(i);
                        JSONObject currentRecipeTime = currentRecipe.getJSONObject("time");
                        JSONArray currentRecipeIngredients = currentRecipe.getJSONArray("ingredients");
                        JSONArray currentRecipeSteps = currentRecipe.getJSONArray("steps");
                        JSONObject currentRecipeNutrition = currentRecipe.getJSONObject("nutrition");

                        ArrayList<Ingredient> ingredients = new ArrayList<>();

                        for (int j=0; j<currentRecipeIngredients.length(); j++)
                        {
                            JSONObject ingredient = currentRecipeIngredients.getJSONObject(j);
                            ingredients.add(new Ingredient(ingredient.getInt("quantity"),ingredient.getString("unit"),ingredient.getString("name")));
                        }

                        ArrayList<Step> steps = new ArrayList<>();

                        for (int j=0; j<currentRecipeSteps.length(); j++)
                        {
                            JSONObject step = currentRecipeSteps.getJSONObject(j);
                            steps.add(new Step(step.getInt("order"),step.getString("step")));
                        }

                        Nutrition nutrition = new Nutrition(currentRecipeNutrition.getDouble("kcal"), currentRecipeNutrition.getDouble("protein"), currentRecipeNutrition.getDouble("fat"), currentRecipeNutrition.getDouble("carbohydrate"), currentRecipeNutrition.getDouble("sugar"), currentRecipeNutrition.getDouble("sat_fat"), currentRecipeNutrition.getDouble("fiber"), currentRecipeNutrition.getDouble("sodium"));

                        listRecipes.add(new Recipe(i, currentRecipe.getString("title"), currentRecipe.getString("picture_url"), currentRecipe.getInt("portions"), new Timing(currentRecipeTime.getInt("total"),currentRecipeTime.getInt("prep"),currentRecipeTime.getInt("baking")), ingredients, steps, nutrition));
                    }
                    displayRecyclerView();
                }
                catch(JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
}
