package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FourthActivity extends AppCompatActivity {

    private TextView tvName, tvType, tvCapacity, tvRating, tvStatus;
    private LinearLayout layoutDetails;

    private final ActivityResultLauncher<Intent> addBarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Preluăm obiectul Bar din intent (folosind Serializable)
                    Bar receivedBar = (Bar) result.getData().getSerializableExtra("BAR_OBJECT");
                    
                    if (receivedBar != null) {
                        // Afișăm detaliile în TextView-uri
                        tvName.setText("Nume: " + receivedBar.getName());
                        tvType.setText("Tip: " + receivedBar.getType());
                        tvCapacity.setText("Capacitate: " + receivedBar.getCapacity());
                        tvRating.setText("Rating: " + receivedBar.getRating() + " stele");
                        tvStatus.setText("Status: " + (receivedBar.isOpen() ? "Deschis" : "Închis"));
                        
                        layoutDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        tvName = findViewById(R.id.tvResultName);
        tvType = findViewById(R.id.tvResultType);
        tvCapacity = findViewById(R.id.tvResultCapacity);
        tvRating = findViewById(R.id.tvResultRating);
        tvStatus = findViewById(R.id.tvResultStatus);
        layoutDetails = findViewById(R.id.layoutDetails);

        Button buttonAdd = findViewById(R.id.buttonAddBar);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(FourthActivity.this, AddBarActivity.class);
            addBarLauncher.launch(intent);
        });
    }
}