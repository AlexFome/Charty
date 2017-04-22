package com.fome.charty.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fome.charty.models.Data;
import com.fome.charty.R;

import java.util.ArrayList;

/**
 * Created by Alex on 09.02.2017.
 */
public class DataBarAdapter extends BaseAdapter {

    ArrayList<Data> data;
    Context context;

    public DataBarAdapter (Context context, ArrayList<Data> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.data_bar, null);
        }

        Data d = data.get(position);
        float fontSize = 15;

        TextView name = (TextView) convertView.findViewById(R.id.dataBar);
        name.setText(d.name);
        name.setTextSize(fontSize);

        Drawable background = name.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(d.color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(d.color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(d.color);
        }

        if (d.name.equals("")) {
            convertView.setVisibility(View.GONE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
