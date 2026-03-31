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

        spTip.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Bere.Type.values()));

        // Verificăm dacă am primit un obiect pentru editare
        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_BERE")) {
            // Modificat: Folosim getParcelableExtra
            Bere bereDeEditat = intent.getParcelableExtra("EDIT_BERE");
            editPosition = intent.getIntExtra("EDIT_POSITION", -1);

            if (bereDeEditat != null) {
                etNume.setText(bereDeEditat.getNume());
                etCantitate.setText(String.valueOf(bereDeEditat.getCantitate()));
                cbAlcoholica.setChecked(bereDeEditat.isAlcoholica());
                rbRating.setRating((float) bereDeEditat.getRating());
                
                // Setăm spinner-ul pe tipul corespunzător
                Bere.Type tip = bereDeEditat.getTip();
                for (int i = 0; i < Bere.Type.values().length; i++) {
                    if (Bere.Type.values()[i] == tip) {
                        spTip.setSelection(i);
                        break;
                    }
                }

                // Setăm DatePicker-ul
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
            // Obiectul Bere este acum Parcelable
            resultIntent.putExtra("BERE_OBJECT", bereRezultat);
            resultIntent.putExtra("EDIT_POSITION", editPosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
