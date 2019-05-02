package com.ynov.dietynov;

import java.io.Serializable;

public class Timing implements Serializable {
    private int total;
    private int prep;
    private int baking;

    public Timing(int _total, int _prep, int _baking) {
        this.total = _total;
        this.prep = _prep;
        this.baking = _baking;
    }

    public int getBaking() {
        return baking;
    }

    public int getPrep() {
        return prep;
    }

    public int getTotal() {
        return total;
    }

    public void setBaking(int baking) {
        this.baking = baking;
    }

    public void setPrep(int prep) {
        this.prep = prep;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
