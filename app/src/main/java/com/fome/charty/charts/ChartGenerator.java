package com.fome.charty.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ListView;

import com.fome.charty.models.Data;
import com.fome.charty.adapters.DataBarAdapter;

import java.util.ArrayList;

/**
 * Created by Alex on 14.02.2017.
 */
public class ChartGenerator extends View {

    public Context context;
    public ArrayList<Data> data;

    public Canvas canvas;

    public float height;
    public float width;

    public ArrayList<Data> leftBar = new ArrayList<>();
    public ArrayList<Data> rightBar = new ArrayList<>();
    public ListView[] dataBars;
    public DataBarAdapter[] dataBarAdapters;

    public ChartGenerator(Context context) {
        super(context);
    }
}
