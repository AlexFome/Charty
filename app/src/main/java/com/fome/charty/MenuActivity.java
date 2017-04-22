package com.fome.charty;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.fome.charty.adapters.OptionsAdapter;
import com.fome.charty.models.Option;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ArrayList<Option> chartOptions;
    GridView optionsGrid;

    boolean mPicking;

    Gson gson = new Gson();

    final int PERMISSION_REQUEST_WRITE_STORAGE = 1;
    final int PERMISSION_REQUEST_READ_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        PermissionManager.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_STORAGE, this, this);

        Intent intent = getIntent();
        FacebookSdk.sdkInitialize(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();

        if (intent != null) {
            if (Intent.ACTION_PICK.equals(intent.getAction())) {
                mPicking = true;
                MessengerThreadParams mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
                String metadata = mThreadParams.metadata;
                List<String> participantIds = mThreadParams.participants;
            }
        }

        chartOptions = new ArrayList<>();
        chartOptions.add(new Option(R.string.pie_chart, "Pie chart"));
        chartOptions.add(new Option(R.string.area_chart, "Graph chart"));
        chartOptions.add(new Option(R.string.bar_chart, "Bar chart"));
        chartOptions.add(new Option(R.string.progress_chart, "Progress chart"));
        chartOptions.add(new Option(R.string.save, "Saved charts"));
        chartOptions.add(new Option(R.string.settings, "Settings"));

        optionsGrid = (GridView) findViewById(R.id.chartOptions);
        optionsGrid.setAdapter(
                new OptionsAdapter(getApplicationContext(), chartOptions)
        );
        optionsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 4) {
                    selectChartType(position, 1);
                } else {
                    switch (position) {
                        case 4:
                            loadActivity(SavedChartsMenu.class, 0, 2);
                            break;
                        case 5:
                            loadActivity(SettingsMenu.class, 0, 3);
                            break;
                    }
                }
            }
        });

    }

    void selectChartType (int type, int requestNum) {
        loadActivity(ChartGeneratorMenu.class, type, requestNum);
    }

    void loadActivity (Class activity, int type, int requestNum) {
        Intent intent = new Intent(MenuActivity.this, activity);
        intent.putExtra("type", type);
        intent.putExtra("mPicking", mPicking);
        if (!mPicking) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestNum);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:

                if(resultCode == Activity.RESULT_OK) {

                    ShareToMessengerParams shareToMessengerParams =
                            ShareToMessengerParams.newBuilder(Uri.fromFile(new File(data.getExtras().getString("Path"))), "image/jpeg")
                                    .build();

                    MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
                }

                break;
            case 2:

                if(resultCode == Activity.RESULT_OK) {

                    Intent intent = new Intent(getApplicationContext(), ChartGeneratorMenu.class);
                    intent.putExtra("chart", data.getExtras().getString("chart"));
                    intent.putExtra("mPicking", mPicking);

                    startActivityForResult(intent, 1);

                }

                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_STORAGE: {

            }

            case PERMISSION_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionManager.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_WRITE_STORAGE, this, this);
                }
                break;
            }

        }
    }

}
