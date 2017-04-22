package com.fome.charty.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import com.fome.charty.models.Data;
import com.fome.charty.R;
import com.fome.charty.adapters.DataBarAdapter;

import java.util.ArrayList;

/**
 * Created by Alex on 07.02.2017.
 */
public class PieChartGenerator extends ChartGenerator {

    RectF rectf;
    RectF innerRectF;

    public PieChartGenerator(Context context, ArrayList<Data> data, ListView[] dataBars) {
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

        float size = getWidth() * 0.8f;
        Point center = new Point(getWidth()/2, getHeight()/2);

        rectf = new RectF( center.x - size/2, center.y - size/2, center.x + size/2, center.y + size/2);
        size *= 0.7f;
        innerRectF = new RectF(center.x - size/2, center.y - size/2, center.x + size/2, center.y + size/2);

        float sum=0;
        for(int i=0; i < data.size(); i++)
        {
            sum += data.get(i).value;
        }
        for(int i=0; i < data.size (); i++)
        {
            data.get(i).chartSize = 360 * (data.get(i).value / sum);
        }

        Paint chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setAntiAlias(true);
        chartPaint.setColor(Color.RED);

        float step = 90;
        for (int i = 0; i < data.size(); i++) {
            chartPaint.setColor(data.get (i).color);
            canvas.drawArc (rectf, step, data.get(i).chartSize + 1, true, chartPaint);
            step += data.get(i).chartSize;
        }

        chartPaint.setColor(ContextCompat.getColor(context, R.color.white));
        canvas.drawOval(innerRectF, chartPaint);

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
