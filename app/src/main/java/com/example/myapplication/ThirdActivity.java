package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Preluăm datele trimise prin Intent
        Bundle bundleIn = getIntent().getExtras();
        int suma = 0;
        if (bundleIn != null) {
            String mesaj = bundleIn.getString("mesaj", "Niciun mesaj");
            int val1 = bundleIn.getInt("valoare1", 0);
            int val2 = bundleIn.getInt("valoare2", 0);
            suma = val1 + val2;

            // Afișăm Toast-ul
            String textAfisat = "Mesaj: " + mesaj + "\nVal1: " + val1 + ", Val2: " + val2;
            Toast.makeText(this, textAfisat, Toast.LENGTH_LONG).show();
        }

        final int finalSuma = suma;
        Button buttonBack = findViewById(R.id.buttonBackToSecond);
        buttonBack.setOnClickListener(v -> {
            // Creăm un Intent pentru rezultat
            Intent returnIntent = new Intent();
            Bundle bundleOut = new Bundle();
            bundleOut.putString("mesaj_retur", "Suma a fost calculată cu succes!");
            bundleOut.putInt("suma", finalSuma);
            returnIntent.putExtras(bundleOut);

            // Setăm rezultatul și închidem activitatea
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}