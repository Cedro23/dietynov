package com.ynov.dietynov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "android_dietynov.db";

    private static final String MEASUREMENTS_TABLE = "measurements_table";
    private static final String RECIPES_TABLE = "recipes_table";

    //Champs tables measurements_table et recipes_table
    private static final String _ID = "id";

    //Champs measurements_table
    private static final String _TYPE = "type";
    private static final String _DATE = "date";
    private static final String _VALUE = "value";

    public DatabaseHelper(Context _context)
    {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = _context;
    }

    private static final String CREATE_TABLE_MEASUREMENTS_TBL = "CREATE TABLE " + MEASUREMENTS_TABLE + "(" + _ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " + _TYPE + " VARCHAR(10) NOT NULL ," + _DATE + " INTEGER(8) NOT NULL, " + _VALUE + " REAL NOT NULL)";
    //A décomenter lors de la mise en place de la deuxième table
//    private static final String CREATE_TABLE_RECIPES_TBL = "";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEASUREMENTS_TBL);
        //A décomenter lors de la mise en place de la deuxième table
//        db.execSQL(CREATE_TABLE_RECIPES_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENTS_TABLE);
        //A décomenter lors de la mise en place de la deuxième table
//        db.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE);

        onCreate(db);
    }

    //insert data in measurements table
    public void insertDataInMeasurements(String _type, long _date, float _value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(_TYPE, _type); //Add type
        cv.put(_DATE, _date); //Add date
        cv.put(_VALUE, _value); //Add value

        db.insert(MEASUREMENTS_TABLE, null, cv);
        db.close();
    }

    //fetch all of one type from measurements
    public ArrayList<MeasurementData> fetchAllOfOneTypeFromMeasurements(String _type)
    {
        ArrayList<MeasurementData> listMeasurementData = new ArrayList<>();

        String selectQuery = "SELECT  date, value FROM " + MEASUREMENTS_TABLE + " WHERE type = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery,new String[] {_type},null);

        if (c.moveToFirst()) {
            do {
                listMeasurementData.add(new MeasurementData(c.getInt(0), c.getFloat(1)));
            } while (c.moveToNext());
        }

        c.close();

        return listMeasurementData;
    }

    //delete all of one type from measurements
    public void deleteDataFromOneInMeasurements(String _type)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(MEASUREMENTS_TABLE, "type = ?", new String[] {_type});
        db.close();
    }





    //Fetch all recipes from Recipe table
    public ArrayList<Recipe> fetchAllFromRecipe()
    {
        ArrayList<Recipe> listRecipes = new ArrayList<>();



        return listRecipes;
    }

    //Fetch one from recipe
//    public Recipe fetchOneFromRecipe()
//    {
//        Recipe recipe;
//
//
//
//        return recipe;
//    }

    //Vérifie combien de fois le prospect se trouve dans la BDD
    public boolean getProspectIsFav(int _idRecipe)
    {
        String selectQuery = "SELECT  * FROM " + RECIPES_TABLE + " WHERE id_recipe = " + _idRecipe;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null,null);

        //Si la recette n'est pas présente dans la BDD, alors on renvoie "false"
        if (cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        //Si la recette est présente dans la BDD au moins une fois (max = 1), alors on revoie "true"
        else
        {
            cursor.close();
            return true;
        }
    }
}
