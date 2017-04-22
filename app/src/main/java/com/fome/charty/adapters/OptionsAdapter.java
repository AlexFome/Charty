package com.fome.charty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fome.charty.FontManager;
import com.fome.charty.models.Option;
import com.fome.charty.R;

import java.util.ArrayList;

/**
 * Created by Alex on 07.02.2017.
 */
public class OptionsAdapter  extends BaseAdapter {

    Context context;
    ArrayList<Option> options;

    public OptionsAdapter (Context context, ArrayList<Option> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Option option = options.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.option, null);
        }

        TextView mainText = (TextView) convertView.findViewById(R.id.mainText);
        TextView additionalText = (TextView) convertView.findViewById(R.id.additionalText);

        if (option.isIcon) {
            mainText.setText(option.mainIcon);
            mainText.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        } else {
            mainText.setText(option.mainText);
        }

        additionalText.setText(option.additionalText);
        additionalText.setTypeface(FontManager.getTypeface(context, FontManager.ABEL));

        return convertView;
    }
}
