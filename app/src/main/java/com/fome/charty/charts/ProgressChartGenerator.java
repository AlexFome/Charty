package com.fome.charty.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import com.fome.charty.models.Data;
import com.fome.charty.R;
import com.fome.charty.adapters.DataBarAdapter;

import java.util.ArrayList;

/**
 * Created by Alex on 10.02.2017.
 */
public class ProgressChartGenerator extends ChartGenerator {

    public ProgressChartGenerator(Context context, ArrayList<Data> data, ListView[] dataBars) {
        super(context);

        this.context = context;
        this.data = data;
        this.dataBars = dataBars;

        dataBarAdapters = new DataBarAdapter[] {new DataBarAdapter(context, leftBar), new DataBarAdapter(context, rightBar)};
        dataBars [0].setAdapter(dataBarAdapters [0]);
        dataBars [1].setAdapter(dataBarAdapters [1]);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        this.canvas = canvas;
        refresh();

    }


    void refresh () {

        canvas.drawColor(ContextCompat.getColor(context, R.color.white));

        height = getHeight();
        width = getWidth ();

        float centerY = getHeight() / 2;

        float maxDataValue = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).value > maxDataValue) {
                maxDataValue = data.get (i).value;
            }
        }

        float stepX = width / maxDataValue;
        float stepY = height / 20;
        float lineWidth = stepY / 2;

        Paint paint = new Paint();

        float top = data.size() % 2 == 0 ? centerY - stepY/2 - stepY * (data.size()/2 - 1): centerY - stepY * ((data.size() - 1)/2);

        for (int i = 0; i < data.size(); i++) {
            paint.setColor(data.get(i).color);
            float y = top + stepY * i;
            RectF bar = new RectF (0, y - lineWidth/2, data.get(i).value * stepX, y + lineWidth /2);
            canvas.drawRect(bar, paint);
        }

        rightBar.clear();
        leftBar.clear();

        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            boolean onRightSide = i % 2 == 0 ? true : false;

            if (onRightSide) {
                rightBar.add(d);
            } else {
                leftBar.add(d);
            }
        }

        dataBarAdapters [0].notifyDataSetChanged();
        dataBarAdapters [1].notifyDataSetChanged();

    }
}
