package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private ChartView chartView;
    private Spinner spChartType;
    private List<Float> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chartView = findViewById(R.id.chartView);
        spChartType = findViewById(R.id.spChartType);

        // Receive values from Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float[] valArray = extras.getFloatArray("values");
            if (valArray != null) {
                values = new ArrayList<>();
                for (float v : valArray) {
                    values.add(v);
                }
            }
        }

        if (values == null) {
            values = new ArrayList<>();
            // Default values if none provided
            values.add(10f);
            values.add(20f);
            values.add(30f);
        }

        setupSpinner();
    }

    private void setupSpinner() {
        String[] types = {"PieChart", "ColumnChart", "BarChart"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChartType.setAdapter(adapter);

        spChartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = types[position];
                chartView.setData(values, selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
