package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Bere> listaBeri = new ArrayList<>();
    private BeerAdapter adapter;
    private static final String FILE_NAME = "beri.dat";
    private static final String FAVORITES_FILE = "favorite_beri.dat";

    private final ActivityResultLauncher<Intent> addBeerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bere berePrimita = result.getData().getParcelableExtra("BERE_OBJECT");
                    int editPosition = result.getData().getIntExtra("EDIT_POSITION", -1);

                    if (berePrimita != null) {
                        if (editPosition != -1) {
                            listaBeri.set(editPosition, berePrimita);
                            rewriteAllBeersFile();
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
        Button btnSettings = findViewById(R.id.btnSettings);

        loadBeersFromFile();

        adapter = new BeerAdapter(this, listaBeri);
        lvBeri.setAdapter(adapter);

        lvBeri.setOnItemClickListener((parent, view, position, id) -> {
            Bere bereSelectata = listaBeri.get(position);
            Intent intent = new Intent(MainActivity.this, AddBeerActivity.class);
            intent.putExtra("EDIT_BERE", (Parcelable) bereSelectata);
            intent.putExtra("EDIT_POSITION", position);
            addBeerLauncher.launch(intent);
        });

        // LongItemClick: Salvează în favorite și afișează Toast
        lvBeri.setOnItemLongClickListener((parent, view, position, id) -> {
            Bere bereFavorita = listaBeri.get(position);
            saveToFavorites(bereFavorita);
            Toast.makeText(MainActivity.this, "Adăugat la favorite: " + bereFavorita.getNume(), Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAddBeer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBeerActivity.class);
            addBeerLauncher.launch(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void loadBeersFromFile() {
        listaBeri.clear();
        try (FileInputStream fis = openFileInput(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Bere b = (Bere) ois.readObject();
                    listaBeri.add(b);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void rewriteAllBeersFile() {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Bere b : listaBeri) {
                oos.writeObject(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFavorites(Bere bere) {
        try (FileOutputStream fos = openFileOutput(FAVORITES_FILE, Context.MODE_APPEND);
             ObjectOutputStream oos = new AppendableObjectOutputStream(fos)) {
            oos.writeObject(bere);
        } catch (IOException e) {
            try (FileOutputStream fos = openFileOutput(FAVORITES_FILE, Context.MODE_PRIVATE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(bere);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(FileOutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
