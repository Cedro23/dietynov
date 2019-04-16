package com.ynov.dietynov;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;

public class MyWeightActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<MeasurementData> listMeasurementData;
    private ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_weight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewWeightData();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Références au graphique
        LineChart chart = findViewById(R.id.chart);
        displayChart(chart);



        //Récupération des données dans la BD
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        listMeasurementData = dbHelper.fetchAllOfOneTypeFromMeasurements("Poids");

        displayListView();

        //Gestion des données
        entries = new ArrayList<>();
        for ( MeasurementData mData : listMeasurementData) {
            int dateInDays = secondsToDays(mData.getDate());
            int value = Math.round(mData.getValue());
            entries.add( new Entry(dateInDays,value));
        }
        updateChart(entries, chart);

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
        getMenuInflater().inflate(R.menu.myweight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.deleteDataFromOneInMeasurements("Poids");

            LineChart chart = findViewById(R.id.chart);
            entries.clear();
            updateChart(entries, chart);

            Toast.makeText(this, "Données de poids supprimées", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();


        if (id == R.id.nav_home) {
            // Handle the home action
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.nav_account) {
            // Handle the account action
            intent = new Intent(this, MyAccountActivity.class);
        } else if (id == R.id.nav_recipes) {
            // Handle the recipes action
            intent = new Intent(this, MyRecipesActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        startActivity(intent);
        return true;
    }

    private void addNewWeightData()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Insérer poids");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int currentDate = (int) (System.currentTimeMillis() / 1000) - 1546300800;
                DatabaseHelper dbHelper = new DatabaseHelper(MyWeightActivity.this);
                float value = Integer.valueOf(input.getText().toString());
                dbHelper.insertDataInMeasurements("Poids", currentDate, value);

                LineChart chart = findViewById(R.id.chart);
                entries.add(new Entry(secondsToDays(currentDate), value));
                updateChart(entries, chart);
                displayListView();


                Toast.makeText(MyWeightActivity.this, "Poids ajouté", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.show();
    }

    private int secondsToDays(int _seconds)
    {
        int days = 0;
        double dateInHours = _seconds / 3600;
        days = (int) Math.ceil(dateInHours/24);
        return days;
    }

    private void updateChart(ArrayList<Entry> _entries, LineChart _chart)
    {
        LineDataSet dataSet;
        LineData lineData;
        dataSet = new LineDataSet(_entries, "Poids en fonction de la date");
        dataSet.setCircleRadius(10f);
        dataSet.setCircleHoleRadius(4.5f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData = new LineData(dataSet);
        lineData.setValueTextSize(15f);
        lineData.setValueTextColor(Color.BLACK);
        _chart.setData(lineData);
        _chart.invalidate(); // refresh
    }

    private void displayChart(LineChart _chart)
    {
        //Options du graphique
        _chart.setTouchEnabled(true);
        _chart.setDragEnabled(true);
        _chart.setScaleEnabled(true);
        _chart.setPinchZoom(true);
        _chart.setDoubleTapToZoomEnabled(false);
        _chart.setDrawBorders(true);

        //Options axes
        YAxis rightAxis = _chart.getAxisRight(); //Axe Y côté droit
        rightAxis.setEnabled(false);

        //Axe des ordonnées
        YAxis leftAxis = _chart.getAxisLeft(); //Axe Y côté gauche
        leftAxis.setGranularity(1);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(15f);

        //Axe des abscisses
        XAxis xAxis = _chart.getXAxis(); //Axe X
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(15f);
        xAxis.setValueFormatter(new DayAxisValueFormatter(_chart));

        //Options legende
        Legend legend = _chart.getLegend();
        legend.setEnabled(false);

        //Options description
        Description desc = _chart.getDescription();
        desc.setEnabled(false);
    }

    private void displayListView() {
        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;

        //Partie RecyclerView
        recyclerView = findViewById(R.id.RV_measurements);

        //Ajout de la ligne de division
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        //Instanciation de LinearLayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Instanciation de l'adapter
        mAdapter = new RVAdapaterMeasurements(this, listMeasurementData);
        recyclerView.setAdapter(mAdapter);
    }
}
