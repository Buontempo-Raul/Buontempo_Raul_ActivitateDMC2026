package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;

public class AddBeerActivity extends AppCompatActivity {

    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        EditText etNume = findViewById(R.id.etNume);
        EditText etCantitate = findViewById(R.id.etCantitate);
        CheckBox cbAlcoholica = findViewById(R.id.cbAlcoholica);
        RatingBar rbRating = findViewById(R.id.rbRating);
        Spinner spTip = findViewById(R.id.spTip);
        DatePicker dpDataProductie = findViewById(R.id.dpDataProductie);
        Button btnSave = findViewById(R.id.btnSave);

        applyPreferences();

        spTip.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Bere.Type.values()));

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_BERE")) {
            Bere bereDeEditat = intent.getParcelableExtra("EDIT_BERE");
            editPosition = intent.getIntExtra("EDIT_POSITION", -1);

            if (bereDeEditat != null) {
                etNume.setText(bereDeEditat.getNume());
                etCantitate.setText(String.valueOf(bereDeEditat.getCantitate()));
                cbAlcoholica.setChecked(bereDeEditat.isAlcoholica());
                rbRating.setRating((float) bereDeEditat.getRating());
                
                Bere.Type tip = bereDeEditat.getTip();
                for (int i = 0; i < Bere.Type.values().length; i++) {
                    if (Bere.Type.values()[i] == tip) {
                        spTip.setSelection(i);
                        break;
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(bereDeEditat.getDataProductie());
                dpDataProductie.init(calendar.get(Calendar.YEAR), 
                                  calendar.get(Calendar.MONTH), 
                                  calendar.get(Calendar.DAY_OF_MONTH), null);
                
                btnSave.setText("Actualizează");
            }
        }

        btnSave.setOnClickListener(v -> {
            String nume = etNume.getText().toString();
            String cantitateStr = etCantitate.getText().toString();

            if (nume.isEmpty() || cantitateStr.isEmpty()) {
                Toast.makeText(this, "Vă rugăm să completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantitate = Integer.parseInt(cantitateStr);
            boolean alcoholica = cbAlcoholica.isChecked();
            double rating = rbRating.getRating();
            Bere.Type tip = (Bere.Type) spTip.getSelectedItem();

            Calendar calendar = Calendar.getInstance();
            calendar.set(dpDataProductie.getYear(), dpDataProductie.getMonth(), dpDataProductie.getDayOfMonth());
            Date dataProductie = calendar.getTime();

            Bere bereRezultat = new Bere(nume, cantitate, alcoholica, rating, tip, dataProductie);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("BERE_OBJECT", (Parcelable) bereRezultat);
            resultIntent.putExtra("EDIT_POSITION", editPosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void applyPreferences() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        float textSize = prefs.getFloat("text_size", 18);
        String textColor = prefs.getString("text_color", "#000000");

        int colorValue = Color.BLACK;
        try {
            colorValue = Color.parseColor(textColor);
        } catch (Exception ignored) {}

        TextView[] textViews = {
            findViewById(R.id.etNume),
            findViewById(R.id.etCantitate),
            findViewById(R.id.cbAlcoholica)
        };

        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tv.setTextColor(colorValue);
        }
    }
}
