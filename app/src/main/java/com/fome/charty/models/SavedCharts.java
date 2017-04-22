package com.fome.charty.models;

import java.util.ArrayList;

/**
 * Created by Alex on 12.02.2017.
 */
public class SavedCharts {
    public ArrayList<SavedChart> chartsList;

    public SavedCharts () {
        chartsList = new ArrayList<>();
    }

    public SavedCharts (ArrayList<SavedChart> chartsList) {
        this.chartsList = chartsList;
    }

}
