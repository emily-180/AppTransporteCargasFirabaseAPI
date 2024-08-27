package br.edu.ifsuldeminas.mch.apptransportecargas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.edu.ifsuldeminas.mch.apptransportecargas.db.DAOObserver;
import br.edu.ifsuldeminas.mch.apptransportecargas.db.UserDAO;
import br.edu.ifsuldeminas.mch.apptransportecargas.model.User;

public class ChangePasswordActivity extends AppCompatActivity implements DAOObserver{

    private EditText editTextUsername, editTextNewPassword;
    private Button buttonChangePassword;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);

        userDAO = new UserDAO(new DAOObserver() {
            @Override
            public void loadOk(List<User> users) {}

            @Override
            public void loadErro() {
                Toast.makeText(ChangePasswordActivity.this, "Erro ao carregar usuários", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void saveOK() {}

            @Override
            public void saveErro() {}
            @Override
            public void updateOK() {
                Toast.makeText(ChangePasswordActivity.this, R.string.msgSenhaSucesso, Toast.LENGTH_SHORT).show();
                userDAO.loadUsers();
                finish();
            }

            @Override
            public void updateErro() {
                Toast.makeText(ChangePasswordActivity.this, R.string.msgSenhaSucesso, Toast.LENGTH_SHORT).show();
            }

        });

        buttonChangePassword.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String newPassword = editTextNewPassword.getText().toString();

            if (username.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, R.string.msgLoginCampoVazio, Toast.LENGTH_SHORT).show();
                return;
            }

            userDAO.updateUserPassword(username, newPassword);
        });
    }

    private User findUser(String username) {

        return null;
    }

    @Override
    public void loadOk(List<User> users) {

    }

    @Override
    public void loadErro() {

    }

    @Override
    public void saveOK() {

    }

    @Override
    public void saveErro() {

    }

    @Override
    public void updateOK() {

    }

    @Override
    public void updateErro() {

    }
}
