package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> secondActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    if (bundle != null) {
                        String mesajFinal = bundle.getString("mesaj_retur");
                        int sumaFinala = bundle.getInt("suma");
                        
                        // Afișăm Toast-ul în activitatea inițială
                        Toast.makeText(this, "Rezultat Final în MainActivity:\n" + 
                                mesajFinal + " Suma: " + sumaFinala, Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonGoToSecond = findViewById(R.id.buttonGoToSecond);
        buttonGoToSecond.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            secondActivityLauncher.launch(intent);
        });
    }
}