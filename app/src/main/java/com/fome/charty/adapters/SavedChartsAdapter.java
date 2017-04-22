package com.fome.charty.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fome.charty.ChartGeneratorMenu;
import com.fome.charty.FontManager;
import com.fome.charty.R;
import com.fome.charty.models.SavedChart;
import com.fome.charty.models.SavedCharts;
import com.fome.charty.SavedChartsMenu;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Alex on 11.02.2017.
 */
public class SavedChartsAdapter extends BaseAdapter {

    Context context;
    ArrayList<SavedChart> savedCharts;

    boolean mPicking;

    Gson gson;

    public SavedChartsAdapter (Context context, ArrayList<SavedChart> savedCharts, boolean mPicking) {
        this.context = context;
        this.savedCharts = savedCharts;
        this.mPicking = mPicking;

        gson = new Gson ();
    }

    @Override
    public int getCount() {
        return savedCharts.size();
    }

    @Override
    public Object getItem(int position) {
        return savedCharts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.saved_chart, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.chartName);
        TextView type = (TextView) convertView.findViewById(R.id.chartType);
        TextView remove = (TextView) convertView.findViewById(R.id.removeChart);

        name.setTypeface(FontManager.getTypeface(context, FontManager.ABEL));
        type.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        remove.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));

        name.setText(savedCharts.get(position).name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChartGeneratorMenu.class);
                intent.putExtra("chart", gson.toJson(savedCharts.get(position), SavedChart.class));
                intent.putExtra("mPicking", mPicking);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (!mPicking) {
                    context.startActivity(intent);
                } else {
                    ((Activity) context).setResult(((Activity) context).RESULT_OK, intent);
                    ((Activity) context).finish();
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedCharts.get(position).file != null && savedCharts.get(position).file.exists())
                savedCharts.get(position).file.delete();

                savedCharts.remove(position);

                String json = gson.toJson(new SavedCharts(savedCharts), SavedCharts.class);
                String prefName = context.getResources().getString(R.string.saved_charts);
                SharedPreferences preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
                preferences.edit().putString(prefName, json).commit();

                notifyDataSetChanged();

                if (savedCharts.size() == 0) {
                    ((SavedChartsMenu)context).noSavedCharts();
                }


            }
        });

        switch (savedCharts.get(position).type) {
            case (0):
                type.setText(context.getResources().getString(R.string.pie_chart));
                break;
            case (1):
                type.setText(context.getResources().getString(R.string.line_chart));
                break;
            case (2):
                type.setText(context.getResources().getString(R.string.bar_chart));
                break;
            case (3):
                type.setText(context.getResources().getString(R.string.progress_chart));
                break;
        }

        return convertView;

    }

}
