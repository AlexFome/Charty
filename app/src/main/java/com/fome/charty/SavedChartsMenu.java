package com.fome.charty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fome.charty.adapters.SavedChartsAdapter;
import com.fome.charty.models.SavedCharts;
import com.google.gson.Gson;

public class SavedChartsMenu extends AppCompatActivity {

    SavedCharts savedCharts;

    ListView chartsListView;
    TextView noChartsSaved;
    SavedChartsAdapter savedChartsAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;

    Gson gson;

    boolean mPicking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_charts_menu);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            mPicking = bundle.getBoolean("mPicking");
        }

        chartsListView = (ListView) findViewById(R.id.chartsList);
        noChartsSaved = (TextView) findViewById(R.id.noChartsSaved);
        noChartsSaved.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.ABEL));
        noChartsSaved.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main));

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.saved_charts), MODE_PRIVATE);
        preferenceEditor = sharedPreferences.edit();
        gson = new Gson ();

        refresh();

    }

    void refresh () {
        String json = sharedPreferences.getString(getResources().getString(R.string.saved_charts), null);
        savedCharts = gson.fromJson(json, SavedCharts.class);

        if (savedCharts != null && savedCharts.chartsList != null && savedCharts.chartsList.size() > 0) {
            savedChartsAdapter = new SavedChartsAdapter(this, savedCharts.chartsList, mPicking);
            chartsListView.setVisibility(View.VISIBLE);
            chartsListView.setAdapter(savedChartsAdapter);
            noChartsSaved.setVisibility(View.GONE);
        } else {
            noChartsSaved.setVisibility(View.VISIBLE);
            chartsListView.setVisibility(View.GONE);
        }
    }

    public void noSavedCharts () {
        noChartsSaved.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

}
