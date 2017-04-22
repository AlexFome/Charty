package com.fome.charty.models;

/**
 * Created by Alex on 07.02.2017.
 */
public class Data {
    public String name;
    public float value;
    public float chartSize;
    public int color;

    public Data (String name, final float value, int color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

}
