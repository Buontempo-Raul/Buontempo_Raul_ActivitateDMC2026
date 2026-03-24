package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Bere> listaBeri = new ArrayList<>();
    private BeerAdapter adapter;

    private final ActivityResultLauncher<Intent> addBeerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bere berePrimita = (Bere) result.getData().getSerializableExtra("BERE_OBJECT");
                    if (berePrimita != null) {
                        listaBeri.add(berePrimita);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvBeri = findViewById(R.id.lvBeri);
        Button btnAddBeer = findViewById(R.id.btnAddBeer);
        Button btnGoToSecond = findViewById(R.id.btnGoToSecond);
        Button btnGoToFourth = findViewById(R.id.btnGoToFourth);

        adapter = new BeerAdapter(this, listaBeri);
        lvBeri.setAdapter(adapter);

        lvBeri.setOnItemClickListener((parent, view, position, id) -> {
            Bere bereSelectata = listaBeri.get(position);
            Toast.makeText(MainActivity.this, "Bere selectată:\n" + bereSelectata.toString(), Toast.LENGTH_LONG).show();
        });

        lvBeri.setOnItemLongClickListener((parent, view, position, id) -> {
            Bere bereStearsa = listaBeri.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Obiect șters: " + bereStearsa.getNume(), Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAddBeer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBeerActivity.class);
            addBeerLauncher.launch(intent);
        });

        btnGoToSecond.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        btnGoToFourth.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FourthActivity.class);
            startActivity(intent);
        });
    }
}
