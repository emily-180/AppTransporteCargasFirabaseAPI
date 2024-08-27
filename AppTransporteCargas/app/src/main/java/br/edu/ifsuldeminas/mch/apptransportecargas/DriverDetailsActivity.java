package br.edu.ifsuldeminas.mch.apptransportecargas;

import static br.edu.ifsuldeminas.mch.apptransportecargas.R.id.editTextDestination;
import static br.edu.ifsuldeminas.mch.apptransportecargas.R.id.editTextOrigin;
import static br.edu.ifsuldeminas.mch.apptransportecargas.R.id.listViewHistory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import br.edu.ifsuldeminas.mch.apptransportecargas.adapter.TripAdapter;
import br.edu.ifsuldeminas.mch.apptransportecargas.dao.TripDAO;
import br.edu.ifsuldeminas.mch.apptransportecargas.model.CepResponse;
import br.edu.ifsuldeminas.mch.apptransportecargas.model.Trip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// DriverDetailsActivity.java
public class DriverDetailsActivity extends AppCompatActivity {

    private ListView listViewServices;
    private ListView listViewHistory;
    private TripDAO tripDAO;
    private String selectedService;

    private TextView textViewDriverName;
    private TextView textViewTruckModel;
    private TextView textViewLicensePlate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tripDAO = new TripDAO(this);

        textViewDriverName = findViewById(R.id.textViewDriverName);
        textViewTruckModel = findViewById(R.id.textViewCaminhao);
        textViewLicensePlate = findViewById(R.id.textViewLicensePlate);

        Intent intent = getIntent();
        String driverName = intent.getStringExtra("DRIVER_NAME");
        String truckModel = intent.getStringExtra("TRUCK_MODEL");
        String licensePlate = intent.getStringExtra("LICENSE_PLATE");

        textViewDriverName.setText(driverName);
        textViewTruckModel.setText(truckModel);
        textViewLicensePlate.setText(licensePlate);

        listViewServices = findViewById(R.id.listViewServices);
        String[] services = {"Mudança Residencial, R$250,00", "Transporte de Cargas Empresarial, R$450,00",
                "Entrega Rápida, R$100,00", "Frete para outro estado, > R$2.000,00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, services);
        listViewServices.setAdapter(adapter);

        listViewServices.setOnItemClickListener((parent, view, position, id) -> {
            selectedService = (String) parent.getItemAtPosition(position);
            showAddressInputDialog();
        });
    }

    private void showAddressInputDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_address, null);

        EditText editTextOrigin = dialogView.findViewById(R.id.editTextOrigin);
        EditText editTextDestination = dialogView.findViewById(R.id.editTextDestination);

        new AlertDialog.Builder(this)
                .setTitle(R.string.tituloEnderecoDiaalogo)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String originCep = editTextOrigin.getText().toString();
                    String destinationCep = editTextDestination.getText().toString();

                    if (originCep.isEmpty() || destinationCep.isEmpty()) {
                        Toast.makeText(DriverDetailsActivity.this, R.string.msgAddressVazia, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Verifica se os CEPs são válidos antes de salvar a viagem
                    validateCep(originCep, destinationCep);
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void validateCep(String originCep, String destinationCep) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ViaCepService service = retrofit.create(ViaCepService.class);

        // Verifica o CEP de origem
        Call<CepResponse> originCall = service.getCepDetails(originCep);
        originCall.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getLogradouro() != null) {
                    // CEP de origem válido, agora verifica o CEP de destino
                    checkDestinationCep(destinationCep, originCep);
                } else {
                    Toast.makeText(DriverDetailsActivity.this, R.string.cepInvalido, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                Toast.makeText(DriverDetailsActivity.this, R.string.verificarCEP, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void checkDestinationCep(String destinationCep, String originCep) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ViaCepService service = retrofit.create(ViaCepService.class);

        // Verifica o CEP de destino
        Call<CepResponse> destinationCall = service.getCepDetails(destinationCep);
        destinationCall.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getLogradouro() != null) {
                    // Ambos os CEPs são válidos, chama o método saveTrip
                    saveTrip(originCep, destinationCep, response.body());
                } else {
                    Toast.makeText(DriverDetailsActivity.this, R.string.cepInvalido, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                Toast.makeText(DriverDetailsActivity.this, R.string.verificarCEP, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveTrip(String originCep, String destinationCep, CepResponse destinationDetails) {
        String driverName = getIntent().getStringExtra("DRIVER_NAME");

        // Obter detalhes do CEP de origem
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ViaCepService service = retrofit.create(ViaCepService.class);
        Call<CepResponse> originCall = service.getCepDetails(originCep);
        originCall.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CepResponse originDetails = response.body();

                    // Criar e salvar a viagem
                    Trip trip = new Trip(null, driverName, new Date().toString(), originCep, destinationCep, selectedService);
                    boolean saved = tripDAO.save(trip);

                    if (saved) {
                        Toast.makeText(DriverDetailsActivity.this, R.string.msgAddressConfirmada, Toast.LENGTH_LONG).show();

                        // Passar detalhes para a AddressDetailsActivity
                        Intent intent = new Intent(DriverDetailsActivity.this, AddressDetailsActivity.class);
                        intent.putExtra("ORIGIN_LOGRADOURO", originDetails.getLogradouro());
                        intent.putExtra("ORIGIN_BAIRRO", originDetails.getBairro());
                        intent.putExtra("ORIGIN_CIDADE", originDetails.getLocalidade());
                        intent.putExtra("ORIGIN_ESTADO", originDetails.getUf());
                        intent.putExtra("DESTINO_LOGRADOURO", destinationDetails.getLogradouro());
                        intent.putExtra("DESTINO_BAIRRO", destinationDetails.getBairro());
                        intent.putExtra("DESTINO_CIDADE", destinationDetails.getLocalidade());
                        intent.putExtra("DESTINO_ESTADO", destinationDetails.getUf());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DriverDetailsActivity.this, R.string.msgAddresErro, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                Toast.makeText(DriverDetailsActivity.this, R.string.verificarCEP, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadHistory() {
        List<Trip> trips = tripDAO.loadTrips();
        TripAdapter adapter = new TripAdapter(this, trips);
        listViewHistory.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}