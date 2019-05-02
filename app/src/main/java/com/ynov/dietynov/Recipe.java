package com.ynov.dietynov;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private String imageURL;
    private int portions;
    private Timing time;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private Nutrition nutrition;

    public Recipe(int _id, String _name, String _imageURL, int _portions, Timing _time, ArrayList<Ingredient> _ingredients, ArrayList<Step> _steps, Nutrition _nutrition)
    {
        this.id = _id;
        this.name = _name;
        this.imageURL = _imageURL;
        this.portions = _portions;
        this.time = _time;
        this.ingredients = _ingredients;
        this.steps = _steps;
        this.nutrition = _nutrition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getPortions() {
        return portions;
    }

    public Timing getTime() {
        return time;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public void setTime(Timing time) {
        this.time = time;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }
}
