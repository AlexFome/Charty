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

import com.fome.charty.IColorPick;
import com.fome.charty.R;

/**
 * Created by Alex on 08.02.2017.
 */
public class ColorsAdapter extends BaseAdapter {

    Context context;
    int[] colors;

    IColorPick colorSelection;

    public ColorsAdapter (Context context, int[] colors, IColorPick colorSelection) {
        this.context = context;
        this.colors = colors;
        this.colorSelection = colorSelection;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.color_option, null);
        }

        Drawable background = convertView.findViewById(R.id.color).getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(colors[position]);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(colors[position]);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(colors[position]);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelection.changeColor(colors[position]);
            }
        });

        return convertView;
    }
}
