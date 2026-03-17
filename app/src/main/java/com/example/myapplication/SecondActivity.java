package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "LifecycleDemo";

    private final ActivityResultLauncher<Intent> thirdActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    if (bundle != null) {
                        String mesajRetur = bundle.getString("mesaj_retur");
                        int suma = bundle.getInt("suma");
                        Toast.makeText(this, mesajRetur + " Suma: " + suma, Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, "onCreate() - DEBUG log");

        Button buttonToThird = findViewById(R.id.buttonToThird);
        buttonToThird.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("mesaj", "Salut din SecondActivity!");
            bundle.putInt("valoare1", 42);
            bundle.putInt("valoare2", 100);
            intent.putExtras(bundle);
            
            // Folosim launcher-ul pentru a porni activitatea și a aștepta rezultatul
            thirdActivityLauncher.launch(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() - INFO log");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume() - VERBOSE log");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause() - WARNING log");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop() - ERROR log");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - DEBUG log");
    }
}