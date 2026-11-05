package com.example.dssmv_projectdroid_1240908_1241237.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.ui.main.MainActivity;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        tvCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            fazerLogin(email, password);
        });
    }

    private void fazerLogin(String email, String password) {
        String query = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        ApiClient.getApiService().fazerLogin(query).enqueue(new Callback<List<Utilizador>>() {
            @Override
            public void onResponse(Call<List<Utilizador>> call, Response<List<Utilizador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Utilizador> utilizadores = response.body();

                    if (utilizadores.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Email ou password incorretos!", Toast.LENGTH_SHORT).show();
                    } else {
                        Utilizador utilizadorLogado = utilizadores.get(0);

                        // Gravação da sessão sem a foto
                        android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        android.content.SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("is_logged_in", true);
                        editor.putString("user_id", utilizadorLogado.getId());
                        editor.putString("user_nome", utilizadorLogado.getNome());
                        editor.putString("user_email", utilizadorLogado.getEmail());
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Bem-vindo, " + utilizadorLogado.getNome() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Erro no servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Utilizador>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Falha de rede. Verifica a internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}