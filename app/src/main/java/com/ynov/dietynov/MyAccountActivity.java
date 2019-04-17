package com.ynov.dietynov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Need to check for shared preferences where these infos will be stocked
    //If shared preferences are set then user has to press reset button to modify them
    //Else he has to fill the form and press continue to save his credentials/datas

    private SharedPreferences accountPreferences;
    private android.content.SharedPreferences.Editor accountPrefsEditor;

    private EditText et_Date;
    private EditText et_Height;
    private EditText et_Weight;
    private RadioGroup rg_Sexe;

    private RadioGroup.OnCheckedChangeListener checkedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Textwatcher pour le format de la date
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "jjmmaaaa";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    et_Date.setText(current);
                    et_Date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };

        //Références aux view de mon layout
        et_Date = findViewById(R.id.ET_dob);
        et_Date.addTextChangedListener(tw);
        et_Height = findViewById(R.id.ET_height);
        et_Weight = findViewById(R.id.ET_weight);
        rg_Sexe = findViewById(R.id.RG_sexe);


        //Gestion des shared preferences pour les données de l'utilisateur
        accountPreferences = getSharedPreferences("accountPrefs", MODE_PRIVATE);
        accountPrefsEditor = accountPreferences.edit();

        //Si il y a des valeurs dans les shared préférences elles seront affichées dans les edit text
        et_Date.setText(accountPreferences.getString("date", ""));
        et_Height.setText(accountPreferences.getString("height", ""));
        et_Weight.setText(accountPreferences.getString("weight", ""));
        if (accountPreferences.getInt("sexe", 2) != 2) {
            ((RadioButton)rg_Sexe.getChildAt(accountPreferences.getInt("sexe", 2))).setChecked(true);
        }

        //Gestion des listener pour les edit text
        et_Date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String date = et_Date.getText().toString();
                    Pattern p = Pattern.compile("[a-zA-Z]");
                    Matcher m = p.matcher(date);
                    if (m.find()){
                        et_Date.setText("");
                    }
                    else{
                        accountPrefsEditor.putString("date", date);
                        accountPrefsEditor.commit();
                    }
                }
            }
        });
        et_Height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String height = et_Height.getText().toString();
                    accountPrefsEditor.putString("height", height);
                    accountPrefsEditor.commit();
                }
            }
        });
        et_Weight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String weight = et_Weight.getText().toString();
                        accountPrefsEditor.putString("weight", weight);
                        accountPrefsEditor.commit();

                        //Permet de baisser/enlever le clavier après avoir appuyer sur ok
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow( et_Weight.getWindowToken(), 0);
                        return true;
                }
                return false;
            }
        });

        checkedChangeListener = new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    accountPrefsEditor.putInt("sexe", group.indexOfChild(checkedRadioButton));
                    accountPrefsEditor.commit();
                }
            }
        };
        rg_Sexe.setOnCheckedChangeListener(checkedChangeListener);
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
        getMenuInflater().inflate(R.menu.myaccount, menu);
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
            resetCredentials();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != R.id.nav_account) {
            Intent intent = new Intent();

            if (id == R.id.nav_home) {
                // Handle the home action
                intent = new Intent(this, MainActivity.class);
            } else if (id == R.id.nav_recipes) {
                // Handle the recipes action
                intent = new Intent(this, MyRecipesActivity.class);
            }
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    // Est lancé lorsque l'utilisateur appuie sur le bouton réinitialiser
    private void resetCredentials()
    {
        et_Date.setText("");
        et_Height.setText("");
        et_Weight.setText("");
        rg_Sexe.setOnCheckedChangeListener(null);
        rg_Sexe.clearCheck();
        rg_Sexe.setOnCheckedChangeListener(checkedChangeListener);
        accountPrefsEditor.clear();
        accountPrefsEditor.commit();
        Toast.makeText(getApplicationContext(),"Données réinitialisées",Toast.LENGTH_SHORT).show();
    }
}
