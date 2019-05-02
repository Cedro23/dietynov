package com.ynov.dietynov;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Récupère l'instance de la toolbar
        setSupportActionBar(toolbar);//Set la toolbar

        //Récupère l'instance du drawer layout (DL)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Gère l'appel à l'ouverture/fermeture du DL
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //Syncronise l'état du DL après la reprise de l'application
        toggle.syncState();

        //Récupère la vue pour l'afficher dans le DL
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Gère la fermeture du DL
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id != R.id.nav_home) {
            Intent intent = new Intent();

            if (id == R.id.nav_account) {
                // Handle the account action
                intent = new Intent(this, MyAccountActivity.class);
            } else if (id == R.id.nav_recipes) {
                // Handle the recipes action
                intent = new Intent(this, MyRecipesActivity.class);
                intent.putExtra("fetchType", "database");
            }
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void displayMyWeight(View _v) {
        Intent intent = new Intent(this, MyWeightActivity.class);
        startActivity(intent);
    }

    public void displayMyMeasurements(View _v) {
        Intent intent = new Intent(this, MyMeasurementsActivity.class);
        startActivity(intent);
    }

    public void displayMyRecipes(View _v) {
        Intent intent = new Intent(this, MyRecipesActivity.class);
        intent.putExtra("fetchType", "webservice");
        startActivity(intent);
    }
}
