package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AddBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bar);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextCapacity = findViewById(R.id.editTextCapacity);
        Spinner spinnerType = findViewById(R.id.spinnerType);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        SwitchMaterial switchOpen = findViewById(R.id.switchOpen);
        Button buttonSave = findViewById(R.id.buttonSave);

        spinnerType.setAdapter(new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_dropdown_item, Bar.Type.values()));

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String capacityStr = editTextCapacity.getText().toString();
            
            if (name.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(this, "Completați numele și capacitatea!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int capacity = Integer.parseInt(capacityStr);
                Bar.Type type = (Bar.Type) spinnerType.getSelectedItem();
                float rating = ratingBar.getRating();
                boolean isOpen = switchOpen.isChecked();

                // Creăm obiectul Bar
                Bar newBar = new Bar(name, capacity, isOpen, (double) rating, type);

                // Returnăm obiectul către prima activitate
                Intent returnIntent = new Intent();
                returnIntent.putExtra("BAR_OBJECT", newBar);
                setResult(RESULT_OK, returnIntent);
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Capacitatea trebuie să fie un număr valid!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}