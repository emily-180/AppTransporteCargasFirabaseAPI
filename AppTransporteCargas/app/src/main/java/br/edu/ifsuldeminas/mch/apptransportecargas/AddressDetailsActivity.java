package br.edu.ifsuldeminas.mch.apptransportecargas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsuldeminas.mch.apptransportecargas.model.CepResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressDetailsActivity extends AppCompatActivity {

    private TextView textViewOriginDetails;
    private TextView textViewDestinationDetails;
    private Button buttonCallDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_details);

        textViewOriginDetails = findViewById(R.id.textViewOriginDetails);
        textViewDestinationDetails = findViewById(R.id.textViewDestinationDetails);
        buttonCallDriver = findViewById(R.id.buttonCallDriver);

        Intent intent = getIntent();
        String originLogradouro = intent.getStringExtra("ORIGIN_LOGRADOURO");
        String originBairro = intent.getStringExtra("ORIGIN_BAIRRO");
        String originCidade = intent.getStringExtra("ORIGIN_CIDADE");
        String originEstado = intent.getStringExtra("ORIGIN_ESTADO");
        String destinoLogradouro = intent.getStringExtra("DESTINO_LOGRADOURO");
        String destinoBairro = intent.getStringExtra("DESTINO_BAIRRO");
        String destinoCidade = intent.getStringExtra("DESTINO_CIDADE");
        String destinoEstado = intent.getStringExtra("DESTINO_ESTADO");

        textViewOriginDetails.setText("Origem: " + originLogradouro + ", " + originBairro + ", " + originCidade + ", " + originEstado);

        textViewDestinationDetails.setText("Destino: " + destinoLogradouro + ", " + destinoBairro + ", " + destinoCidade + ", " + destinoEstado);

        buttonCallDriver.setOnClickListener(v -> {
            Intent callDriverIntent = new Intent(AddressDetailsActivity.this, DriverOnWayActivity.class);
            callDriverIntent.putExtra("ORIGIN_ADDRESS", originLogradouro + ", " + originBairro + ", " + originCidade + ", " + originEstado);
            startActivity(callDriverIntent);
        });
    }
}

