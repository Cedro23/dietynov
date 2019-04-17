package com.ynov.dietynov;

public class Recipe {
    private int id;
    private String name;

    public Recipe(int _id, String _name)
    {
        this.id = _id;
        this.name = _name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
