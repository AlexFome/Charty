package com.fome.charty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.fome.charty.adapters.ColorsAdapter;

public class ColorSelection extends AppCompatActivity implements IColorPick {

    GridView colorsGrid;
    ColorsAdapter colorsAdapter;
    int [] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selection);

        colors = getResources().getIntArray(R.array.dataColors);

        colorsAdapter = new ColorsAdapter(getApplicationContext(), colors, this);
        colorsGrid = (GridView) findViewById(R.id.colorsGrid);
        colorsGrid.setAdapter(colorsAdapter);

    }

    @Override
    public void changeColor (int num) {
        Intent intent = new Intent();
        intent.putExtra("color", num);
        setResult(RESULT_OK, intent);
        finish();
    }
}
