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
public class BarChartGenerator extends ChartGenerator {

    public BarChartGenerator(Context context, ArrayList<Data> data, ListView[] dataBars) {
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

        height = getHeight() * 0.8f;
        width = getWidth ();

        float minDataValue = 0;
        float maxDataValue = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).value < minDataValue) {
                minDataValue = data.get(i).value;
            }
            if (data.get(i).value > maxDataValue) {
                maxDataValue = data.get (i).value;
            }
        }

        float stepX = width / (data.size());
        float stepY = height * 0.8f / maxDataValue;

        Paint paint = new Paint();

        for (int i = 0; i < data.size(); i++) {
            paint.setColor(data.get(i).color);
            RectF bar = new RectF (i * stepX, height - data.get(i).value * stepY, (i + 1) * stepX, height);
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
