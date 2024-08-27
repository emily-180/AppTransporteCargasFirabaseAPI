package br.edu.ifsuldeminas.mch.apptransportecargas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DriverOnWayActivity extends AppCompatActivity {

    private TextView textViewDriverOnWay;
    private Button buttonBackToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_on_way);

        textViewDriverOnWay = findViewById(R.id.textViewDriverOnWay);
        buttonBackToMap = findViewById(R.id.buttonBackToMap);

        String originAddress = getIntent().getStringExtra("ORIGIN_ADDRESS");

        if (originAddress != null) {
            textViewDriverOnWay.setText("Motorista se encaminhando para: " + originAddress);
        }

        buttonBackToMap.setOnClickListener(v -> {
            Intent intent = new Intent(DriverOnWayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
