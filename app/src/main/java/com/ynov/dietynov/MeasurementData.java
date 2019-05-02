package com.ynov.dietynov;

public class MeasurementData {
    private int date;
    private float value;

    public MeasurementData(int _date, float _value) {
        this.date = _date;
        this.value = _value;
    }

    public int getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
