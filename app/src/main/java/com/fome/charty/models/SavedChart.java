package com.fome.charty.models;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 11.02.2017.
 */
public class SavedChart {

    public String name;
    public ArrayList <Data> data;
    public int type;
    public File file;

    public SavedChart(String name, ArrayList <Data> data, int type, File file) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.file = file;
    }

}
