package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText etTextSize = findViewById(R.id.etTextSize);
        EditText etTextColor = findViewById(R.id.etTextColor);
        Button btnSave = findViewById(R.id.btnSaveSettings);

        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        etTextSize.setText(String.valueOf(prefs.getFloat("text_size", 18)));
        etTextColor.setText(prefs.getString("text_color", "#000000"));

        btnSave.setOnClickListener(v -> {
            String sizeStr = etTextSize.getText().toString();
            String colorStr = etTextColor.getText().toString();

            if (sizeStr.isEmpty() || colorStr.isEmpty()) {
                Toast.makeText(this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float size = Float.parseFloat(sizeStr);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("text_size", size);
                editor.putString("text_color", colorStr);
                editor.apply();

                Toast.makeText(this, "Setări salvate!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Dimensiunea trebuie să fie un număr!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
