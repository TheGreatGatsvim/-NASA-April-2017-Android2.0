package com.thegreatgatsvim.nasa_april_2017_android20.models;
/**
 * Created by jorge on 4/29/17.
 */

import java.io.Serializable;

public class Recycle implements Serializable {
    String label, feature;
    int score;
    float co2;
    boolean recyclable;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getCo2() {
        return co2;
    }

    public void setCo2(float co2) {
        this.co2 = co2;
    }

    public boolean isRecyclable() {
        return recyclable;
    }

    public void setRecyclable(boolean recyclable) {
        this.recyclable = recyclable;
    }
}
