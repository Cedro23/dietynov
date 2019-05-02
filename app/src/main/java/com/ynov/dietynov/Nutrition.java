package com.ynov.dietynov;

import java.io.Serializable;

public class Nutrition implements Serializable {
    private double kcal;
    private double protein;
    private double fat;
    private double carbohydrate;
    private double sugar;
    private double sat_fat;
    private double fiber;
    private double sodium;

    public Nutrition(double _kcal, double _protein, double _fat, double _carbohydrate, double _sugar, double _sat_fat, double _fiber, double _sodium) {
        this.kcal = _kcal;
        this.protein = _protein;
        this.fat = _fat;
        this.carbohydrate = _carbohydrate;
        this.sugar = _sugar;
        this.sat_fat = _sat_fat;
        this.fiber = _fiber;
        this.sodium = _sodium;
    }

    public double getKcal() {
        return kcal;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getSugar() {
        return sugar;
    }

    public double getSat_fat() {
        return sat_fat;
    }

    public double getFiber() {
        return fiber;
    }

    public double getSodium() {
        return sodium;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public void setSat_fat(double sat_fat) {
        this.sat_fat = sat_fat;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }
}
