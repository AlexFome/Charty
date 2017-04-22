package com.fome.charty;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.fome.charty.adapters.DataAdapter;
import com.fome.charty.charts.BarChartGenerator;
import com.fome.charty.charts.GraphChartGenerator;
import com.fome.charty.charts.PieChartGenerator;
import com.fome.charty.charts.ProgressChartGenerator;
import com.fome.charty.models.Data;
import com.fome.charty.models.SavedChart;
import com.fome.charty.models.SavedCharts;
import com.google.gson.Gson;

public class ChartGeneratorMenu extends AppCompatActivity implements IColorPick {

    String chartName = "";

    TextView saveChartButton;

    GridView dataSettings;
    ArrayList<Data> data;

    enum ChartType {PIE, GRAPH, BAR, PROGRESS}
    ChartType chartType;
    DataAdapter dataAdapter;

    RelativeLayout chartLayout;
    View chartView;
    LinearLayout chart;

    static ArrayList <Integer> colors;

    int selected;

    View shareToMessenger;

    boolean mPicking;

    Gson gson;

    SavedChart savedChart;

    final int PERMISSION_REQUEST_WRITE_STORAGE = 1;
    final int PERMISSION_REQUEST_READ_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_chart_generator);

        gson = new Gson();

        colors = new ArrayList<>();
        int [] dataColors = getApplication().getResources().getIntArray(R.array.dataColors);
        for (int i = 0; i < dataColors.length; i++) {
            colors.add(dataColors[i]);
        }

        Bundle bundle = getIntent().getExtras();
        ChartType[] values = ChartType.values();

        if (bundle.containsKey("type")) {
            chartType = values[bundle.getInt("type", 0)];
            data = new ArrayList<>();
            Random random = new Random();
            data.add(new Data("Data_1", 2 + random.nextInt(20), getColor()));
            data.add(new Data("Data_2", 2 + random.nextInt(20), getColor()));
            data.add(new Data("Data_3", 2 + random.nextInt(20), getColor()));
        } else if (bundle.containsKey("chart")) {
            savedChart = gson.fromJson(bundle.getString("chart", ""), SavedChart.class);
            data = savedChart.data;
            chartType = values[savedChart.type];
            changeChartName(savedChart.name, true);
        }

        mPicking = bundle.getBoolean("mPicking");

        chart = (LinearLayout) findViewById(R.id.chart);
        dataSettings = (GridView) findViewById(R.id.dataSettings);
        shareToMessenger = findViewById(R.id.messenger_send_button);
        chartLayout = (RelativeLayout) findViewById(R.id.chartRenderer);

        saveChartButton = (TextView) findViewById(R.id.saveChart);
        TextView addData = (TextView) findViewById(R.id.addData);
        TextView deleteData = (TextView) findViewById(R.id.deleteData);

        saveChartButton.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        addData.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.ABEL));
        deleteData.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.ABEL));

        saveChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChart();
            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        shareToMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }
        });

        switch (chartType) {
            case PIE:
                drawPieChart();
                break;
            case GRAPH:
                drawGraphChart();
                break;
            case BAR:
                drawBarChart();
                break;
            case PROGRESS:
                drawProgressChart();
                break;
        }

    }

    void drawPieChart () {
        PieChartGenerator pieChartGenerator = new PieChartGenerator(this, data, new ListView[] {(ListView) chart.findViewById(R.id.leftDataBar), (ListView) chart.findViewById(R.id.rightDataBar)});
        chartView = pieChartGenerator;
        chartLayout.addView(chartView);
        dataAdapter = new DataAdapter(getApplicationContext(), data, chartView, this);
        dataSettings.setAdapter(dataAdapter);
    }

    void drawGraphChart () {
        GraphChartGenerator graphChartGenerator = new GraphChartGenerator(this, data, new ListView[] {(ListView) chart.findViewById(R.id.leftDataBar), (ListView) chart.findViewById(R.id.rightDataBar)});
        chartView = graphChartGenerator;
        chartLayout.addView(chartView);
        dataAdapter = new DataAdapter(getApplicationContext(), data, chartView, this);
        dataSettings.setAdapter(dataAdapter);
    }

    void drawBarChart () {
        BarChartGenerator barChartGenerator = new BarChartGenerator(this, data, new ListView[] {(ListView) chart.findViewById(R.id.leftDataBar), (ListView) chart.findViewById(R.id.rightDataBar)});
        chartView = barChartGenerator;
        chartLayout.addView(chartView);
        dataAdapter = new DataAdapter(getApplicationContext(), data, chartView, this);
        dataSettings.setAdapter(dataAdapter);
    }

    void drawProgressChart () {
        ProgressChartGenerator progressChartGenerator = new ProgressChartGenerator(this, data, new ListView[] {(ListView) chart.findViewById(R.id.leftDataBar), (ListView) chart.findViewById(R.id.rightDataBar)});
        chartView = progressChartGenerator;
        chartLayout.addView(chartView);
        dataAdapter = new DataAdapter(getApplicationContext(), data, chartView, this);
        dataSettings.setAdapter(dataAdapter);
    }

    void addData () {
        Random random = new Random();
        Data newData = new Data("Data_" + (data.size() + 1), 2 + random.nextInt(20), getColor());
        data.add(newData);
        dataAdapter.notifyDataSetChanged();
        dataSettings.smoothScrollToPosition(data.size() - 1);
        chartView.invalidate();
    }

    void deleteData () {

        int dataSize = data.size();
        if (dataSize == 1) return;

        Data removedData = data.get(dataSize - 1);
        data.remove(removedData);
        dataAdapter.notifyDataSetChanged();
        dataSettings.smoothScrollToPosition(data.size() - 1);
        chartView.invalidate();
    }

    void saveChart() {

        if (!PermissionManager.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Toast.makeText(this, "Please, give this app READ STORAGE permission", Toast.LENGTH_LONG).show();
            PermissionManager.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1, this, this);
            return;
        }

        if (!PermissionManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Toast.makeText(this, "Please, give this app WRITE STORAGE permission", Toast.LENGTH_LONG).show();
            PermissionManager.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, this, this);
            return;
        }

        saveChartImageToDevice();
        String toast = savedChart != null ? "Changes are saved" : "Chart is saved on device";
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        saveChartButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
        saveChartButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        saveChartButton.setText(R.string.ok);
    }

    File saveChartImageToDevice () {

        changeChartName(DateFormat.getDateTimeInstance().format(new Date()), false);
        Bitmap b = Bitmap.createBitmap(chart.getWidth(), chart.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        chart.draw(c);

        if (savedChart != null) {
            savedChart.file.delete();
        }

        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Charty");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(directory, chartName + ".jpeg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int type = ChartType.valueOf(chartType.toString()).ordinal();

        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.saved_charts), MODE_PRIVATE);
        SavedCharts savedCharts = gson.fromJson(sharedPreferences.getString(getResources().getString(R.string.saved_charts), null), SavedCharts.class);
        if (savedCharts == null) {
            savedCharts = new SavedCharts();
        }

        boolean isSaved = false;
        for (int i = 0; i < savedCharts.chartsList.size(); i++) {
            SavedChart sc = savedCharts.chartsList.get(i);
            if  (sc.name.equals(chartName)) {
                isSaved = true;
                sc.data = data;
                sc.file = f;
                break;
            }
        }

        if (!isSaved)
        savedCharts.chartsList.add(new SavedChart(chartName, data, type, f));

        String json = gson.toJson(savedCharts, SavedCharts.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.saved_charts), json);
        editor.commit();

        return f;

    }

    void onMessengerButtonClicked() {

        if (!PermissionManager.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Toast.makeText(this, "Please, give this app READ STORAGE permission", Toast.LENGTH_LONG).show();
            PermissionManager.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_STORAGE, this, this);
            return;
        }

        if (!PermissionManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Toast.makeText(this, "Please, give this app WRITE STORAGE permission", Toast.LENGTH_LONG).show();
            PermissionManager.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_WRITE_STORAGE, this, this);
            return;
        }

        int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

        File chart = saveChartImageToDevice();
        Uri chartUri = Uri.fromFile(chart);

        if (mPicking) {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("Path", chart.getAbsolutePath());
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        } else {
            ShareToMessengerParams shareToMessengerParams =
                    ShareToMessengerParams.newBuilder(chartUri, "image/jpeg")
                            .build();

            MessengerUtils.shareToMessenger(
                    this,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
    }

    int getColor () {

        int randomNum = new Random().nextInt(colors.size() - 1);
        int randomColor = colors.get(randomNum);

        colors.remove(randomNum);
        colors.add(colors.size(), randomColor);
        return randomColor;
    }

    @Override
    public void changeColor(int num) {
        selected = num;
        Intent intent = new Intent(getApplicationContext(), ColorSelection.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case 0:
                int color = data.getExtras().getInt("color");
                this.data.get(selected).color = color;
                dataAdapter.notifyDataSetChanged();
                break;
        }
    }

    void changeChartName (String name, boolean override) {
        if (override || chartName.equals("")) {
            chartName = name;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_STORAGE: {

                break;
            }

            case PERMISSION_REQUEST_READ_STORAGE: {
                if (!PermissionManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionManager.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_WRITE_STORAGE, this, this);
                }
                break;
            }

        }
    }

}
