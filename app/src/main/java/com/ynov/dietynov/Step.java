package com.ynov.dietynov;

import java.io.Serializable;

public class Step implements Serializable {
    private int order;
    private String textStep;

    public Step(int _order, String _textStep)
    {
        this.order = _order;
        this.textStep = _textStep;
    }

    public int getOrder() {
        return order;
    }

    public String getTextStep() {
        return textStep;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTextStep(String textStep) {
        this.textStep = textStep;
    }
}
