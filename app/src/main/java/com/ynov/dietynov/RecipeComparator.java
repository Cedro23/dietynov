package com.ynov.dietynov;

import android.content.Context;

import java.util.Comparator;

public class RecipeComparator implements Comparator<Recipe> {
    private final boolean trueLow;
    private final Context mContext;

    public RecipeComparator(boolean trueLow, Context _context) {
        this.trueLow = trueLow;
        this.mContext = _context;
    }

    @Override
    public int compare(Recipe r1, Recipe r2) {
        // TODO Auto-generated method stub
        return (r1.getIsFav(mContext) ^ r2.getIsFav(mContext)) ? ((r1.getIsFav(mContext) ^ this.trueLow) ? 1 : -1) : 0;
    }
}
