package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etChartValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etChartValues = findViewById(R.id.etChartValues);
        Button btnGenerateChart = findViewById(R.id.btnGenerateChart);

        btnGenerateChart.setOnClickListener(v -> {
            String input = etChartValues.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Introduceți câteva valori!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Folosim un regex care prinde orice separator: spațiu, virgulă, punct și virgulă
            String[] parts = input.split("[\\s,;]+");
            List<Float> floatList = new ArrayList<>();

            for (String part : parts) {
                try {
                    // Ignorăm părțile goale care pot apărea de la split
                    if (!part.isEmpty()) {
                        float val = Float.parseFloat(part.replace(',', '.'));
                        floatList.add(val);
                    }
                } catch (NumberFormatException e) {
                    // Ignorăm valorile care nu sunt numere
                }
            }

            if (floatList.size() > 10) {
                Toast.makeText(this, "maxim 10 valori", Toast.LENGTH_SHORT).show();
                return;
            }

            if (floatList.isEmpty()) {
                Toast.makeText(this, "Introduceți numere valide!", Toast.LENGTH_SHORT).show();
                return;
            }

            float[] values = new float[floatList.size()];
            for (int i = 0; i < floatList.size(); i++) {
                values[i] = floatList.get(i);
            }

            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            intent.putExtra("values", values);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_weather) {
            startActivity(new Intent(this, WeatherActivity.class));
            return true;
        } else if (id == R.id.menu_beer_db) {
            startActivity(new Intent(this, BeerDatabaseActivity.class));
            return true;
        } else if (id == R.id.menu_second_activity) {
            startActivity(new Intent(this, SecondActivity.class));
            return true;
        } else if (id == R.id.menu_fourth_activity) {
            startActivity(new Intent(this, FourthActivity.class));
            return true;
        } else if (id == R.id.menu_beer_images) {
            startActivity(new Intent(this, BeerListActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
