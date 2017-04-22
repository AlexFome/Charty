package com.fome.charty.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.fome.charty.models.Data;
import com.fome.charty.FontManager;
import com.fome.charty.IColorPick;
import com.fome.charty.R;

import java.util.ArrayList;

/**
 * Created by Alex on 07.02.2017.
 */
public class DataAdapter extends BaseAdapter {
    Context context;
    ArrayList<Data> data;
    View chartView;

    IColorPick colorPick;

    public DataAdapter (Context context, ArrayList<Data> data, View chartView, IColorPick colorPick) {
        this.context = context;
        this.data = data;
        this.chartView = chartView;
        this.colorPick = colorPick;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Data d = data.get(position);

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.data, null);

            holder.num = (TextView) convertView.findViewById(R.id.dataNum);
            holder.name = (EditText) convertView.findViewById(R.id.dataName);
            holder.value = (EditText) convertView.findViewById(R.id.dataValue);
            holder.color = convertView.findViewById(R.id.pickColor);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;

        holder.num.setText ("" + (position + 1) + ".");
        holder.name.setText (d.name);
        holder.value.setText("" + d.value);

        Drawable background = holder.color.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(d.color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(d.color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(d.color);
        }

        holder.num.setTypeface(FontManager.getTypeface(context, FontManager.ABEL));
        holder.name.setTypeface(FontManager.getTypeface(context, FontManager.ABEL));
        holder.value.setTypeface(FontManager.getTypeface(context, FontManager.ABEL));

        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get (holder.ref).name = s.toString();
                chartView.invalidate();
            }
        });

        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    s.insert(0, "0");
                }
                if (s.toString().charAt(0) == '0' && s.toString().length() > 1 && s.toString().charAt(1) != '.') {
                    s.delete(0,0);
                }
                data.get (holder.ref).value = Float.parseFloat(s.toString());
                chartView.invalidate();
            }
        });

        holder.color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPick.changeColor(position);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView num;
        EditText name;
        EditText value;
        View color;
        int ref;
    }

}
