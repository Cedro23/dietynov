package com.ynov.dietynov;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private double quantity;
    private String unit;
    private String name;

    public Ingredient(double _quantity, String _unit, String _name) {
        this.quantity = _quantity;
        this.unit = _unit;
        this.name = _name;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
