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
                    // Modificat: Folosim getParcelableExtra în loc de getSerializableExtra
                    Bere berePrimita = result.getData().getParcelableExtra("BERE_OBJECT");
                    int editPosition = result.getData().getIntExtra("EDIT_POSITION", -1);

                    if (berePrimita != null) {
                        if (editPosition != -1) {
                            listaBeri.set(editPosition, berePrimita);
                            Toast.makeText(this, "Bere modificată!", Toast.LENGTH_SHORT).show();
                        } else {
                            listaBeri.add(berePrimita);
                            Toast.makeText(this, "Bere adăugată!", Toast.LENGTH_SHORT).show();
                        }
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

        adapter = new BeerAdapter(this, listaBeri);
        lvBeri.setAdapter(adapter);

        lvBeri.setOnItemClickListener((parent, view, position, id) -> {
            Bere bereSelectata = listaBeri.get(position);
            Intent intent = new Intent(MainActivity.this, AddBeerActivity.class);
            // Modificat: Trimiterea obiectului Parcelable
            intent.putExtra("EDIT_BERE", bereSelectata);
            intent.putExtra("EDIT_POSITION", position);
            addBeerLauncher.launch(intent);
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
    }
}
