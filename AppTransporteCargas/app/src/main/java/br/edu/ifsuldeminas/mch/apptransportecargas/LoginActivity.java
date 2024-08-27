package br.edu.ifsuldeminas.mch.apptransportecargas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.edu.ifsuldeminas.mch.apptransportecargas.db.DAOObserver;
import br.edu.ifsuldeminas.mch.apptransportecargas.db.UserDAO;
import br.edu.ifsuldeminas.mch.apptransportecargas.model.User;

public class LoginActivity extends AppCompatActivity implements DAOObserver {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignUp;
    private TextView textViewChangePassword;
    private UserDAO userDAO;
    private List<User> usersList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewChangePassword = findViewById(R.id.textViewChangePassword);

        userDAO = new UserDAO(this);
        userDAO.loadUsers();

        buttonLogin.setOnClickListener(v -> {
            String inputUsername = editTextUsername.getText().toString();
            String inputPassword = editTextPassword.getText().toString();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.msgLoginCampoVazio, Toast.LENGTH_SHORT).show();
                return;
            }

            userDAO.loadUsers();

            if (usersList != null) {
                User user = findUser(inputUsername, inputPassword);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, R.string.msgLoginSucesso, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.msgLoginErro, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
            startActivity(intent);
        });

        textViewChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void loadOk(List<User> users) {
        this.usersList = users;
    }

    @Override
    public void loadErro() {
        Toast.makeText(LoginActivity.this, R.string.txtErroUsuario, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveOK() {}

    @Override
    public void saveErro() {}

    @Override
    public void updateOK() {

    }

    @Override
    public void updateErro() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userDAO.loadUsers();
    }

    private User findUser(String username, String password) {
        if (usersList != null) {
            for (User user : usersList) {
                if (user.getUsername().equals(username) && user.getSenha().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

}
