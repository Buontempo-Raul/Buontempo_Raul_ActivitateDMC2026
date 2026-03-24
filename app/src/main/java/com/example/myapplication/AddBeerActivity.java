package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;

public class AddBeerActivity extends AppCompatActivity {

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

        spTip.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Bere.Type.values()));

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

            Bere bereNoua = new Bere(nume, cantitate, alcoholica, rating, tip, dataProductie);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("BERE_OBJECT", bereNoua);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
