package br.edu.ifsuldeminas.mch.apptransportecargas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.edu.ifsuldeminas.mch.apptransportecargas.db.DAOObserver;
import br.edu.ifsuldeminas.mch.apptransportecargas.db.UserDAO;
import br.edu.ifsuldeminas.mch.apptransportecargas.model.User;

public class SingUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextUsername, editTextPassword;
    private Button buttonSignUp;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        userDAO = new UserDAO(new DAOObserver() {
            @Override
            public void loadOk(List<User> users) {}

            @Override
            public void loadErro() {}

            @Override
            public void saveOK() {
                Toast.makeText(SingUpActivity.this, R.string.msgCadastroSucesso, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SingUpActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void saveErro() {
                Toast.makeText(SingUpActivity.this, R.string.msgCadastroErro, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateOK() {

            }

            @Override
            public void updateErro() {

            }
        });

        buttonSignUp.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SingUpActivity.this, R.string.msgCadastroCampoVazio, Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(name, username, password);
            userDAO.saveUser(user);
        });
    }
}
