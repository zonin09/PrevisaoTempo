package com.example.previsaotempo.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.previsaotempo.R;
import com.example.previsaotempo.model.AppDatabase;
import com.example.previsaotempo.model.User;
import com.example.previsaotempo.model.UserDao;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private UserDao userDao;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                new ValidateLoginTask().execute(username, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                new RegisterUserTask().execute(username, password);
            }
        });
    }

    private class ValidateLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            User user = userDao.getUser(username, password);
            return user != null;
        }

        @Override
        protected void onPostExecute(Boolean isLoggedIn) {
            if (isLoggedIn) {
                // Salva o estado de login
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Fecha a LoginActivity
            } else {
                Toast.makeText(LoginActivity.this, "Credenciais inválidas.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser != null) {
                return false; // Usuário já existe
            } else {
                User user = new User(username, password);
                userDao.insertUser(user);
                return true; // Registro bem-sucedido
            }
        }

        @Override
        protected void onPostExecute(Boolean isRegistered) {
            if (isRegistered) {
                Toast.makeText(LoginActivity.this, "Registrado com sucesso. Faça login agora.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Nome de usuário já existe.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
