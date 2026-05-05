package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Bere> listaBeri = new ArrayList<>();
    private BeerAdapter adapter;
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<Intent> addBeerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bere berePrimita = result.getData().getParcelableExtra("BERE_OBJECT");
                    if (berePrimita != null) {
                        dbHelper.insertBere(berePrimita); // Metoda 1
                        refreshList();
                        Toast.makeText(this, "Bere adăugată în baza de date!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        ListView lvBeri = findViewById(R.id.lvBeri);
        Button btnAddBeer = findViewById(R.id.btnAddBeer);
        Button btnShowAll = findViewById(R.id.btnShowAll);
        Button btnSearchNume = findViewById(R.id.btnSearchNume);
        Button btnFilterInterval = findViewById(R.id.btnFilterInterval);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnBeerImages = findViewById(R.id.btnBeerImages);

        EditText etSearchNume = findViewById(R.id.etSearchNume);
        EditText etMinCant = findViewById(R.id.etMinCant);
        EditText etMaxCant = findViewById(R.id.etMaxCant);
        EditText etDeleteVal = findViewById(R.id.etDeleteVal);
        EditText etUpdateLetter = findViewById(R.id.etUpdateLetter);

        adapter = new BeerAdapter(this, listaBeri);
        lvBeri.setAdapter(adapter);

        // Metoda 2: Selectie toate
        btnShowAll.setOnClickListener(v -> refreshList());

        // Metoda 3: Selectie dupa nume
        btnSearchNume.setOnClickListener(v -> {
            String nume = etSearchNume.getText().toString();
            if (!nume.isEmpty()) {
                Bere b = dbHelper.getBereByNume(nume);
                listaBeri.clear();
                if (b != null) {
                    listaBeri.add(b);
                } else {
                    Toast.makeText(this, "Nu s-a găsit nicio bere cu acest nume", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Metoda 4: Selectie interval
        btnFilterInterval.setOnClickListener(v -> {
            String minStr = etMinCant.getText().toString();
            String maxStr = etMaxCant.getText().toString();
            if (!minStr.isEmpty() && !maxStr.isEmpty()) {
                int min = Integer.parseInt(minStr);
                int max = Integer.parseInt(maxStr);
                List<Bere> filtered = dbHelper.getBeriInInterval(min, max);
                listaBeri.clear();
                listaBeri.addAll(filtered);
                adapter.notifyDataSetChanged();
            }
        });

        // Metoda 5: Stergere
        btnDelete.setOnClickListener(v -> {
            String valStr = etDeleteVal.getText().toString();
            if (!valStr.isEmpty()) {
                int val = Integer.parseInt(valStr);
                dbHelper.deleteBeriMaiMariDecat(val);
                refreshList();
                Toast.makeText(this, "Înregistrări șterse!", Toast.LENGTH_SHORT).show();
            }
        });

        // Metoda 6: Update
        btnUpdate.setOnClickListener(v -> {
            String litera = etUpdateLetter.getText().toString();
            if (!litera.isEmpty()) {
                dbHelper.incrementCantitateByFirstLetter(litera);
                refreshList();
                Toast.makeText(this, "Update realizat!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddBeer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBeerActivity.class);
            addBeerLauncher.launch(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnBeerImages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BeerListActivity.class);
            startActivity(intent);
        });

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_second_activity) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_fourth_activity) {
            Intent intent = new Intent(this, FourthActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_beer_images) {
            Intent intent = new Intent(this, BeerListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshList() {
        listaBeri.clear();
        listaBeri.addAll(dbHelper.getAllBeri());
        adapter.notifyDataSetChanged();
    }
}
