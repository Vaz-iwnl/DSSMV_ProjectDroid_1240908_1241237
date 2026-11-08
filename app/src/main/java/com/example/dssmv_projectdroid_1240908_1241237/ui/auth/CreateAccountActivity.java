package com.example.dssmv_projectdroid_1240908_1241237.ui.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etPassword;
    private Button btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etNome = findViewById(R.id.etNomeRegisto);
        etEmail = findViewById(R.id.etEmailRegisto);
        etPassword = findViewById(R.id.etPasswordRegisto);
        btnCriarConta = findViewById(R.id.btnRegistar);
        btnCriarConta.setOnClickListener(v -> registarAluno());
    }

    private void registarAluno() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Utilizador novoUtilizador = new Utilizador();
        novoUtilizador.setNome(nome);
        novoUtilizador.setEmail(email);
        novoUtilizador.setPassword(password);
        novoUtilizador.setActive(true);
        novoUtilizador.setProfessor(false);

        GymApiService apiService = ApiClient.getApiService();
        Call<Utilizador> call = apiService.criarUtilizador(novoUtilizador);

        call.enqueue(new Callback<Utilizador>() {
            @Override
            public void onResponse(Call<Utilizador> call, Response<Utilizador> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateAccountActivity.this, "Conta de Aluno criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String erroDetalhado = response.errorBody() != null ? response.errorBody().string() : response.message();
                        Toast.makeText(CreateAccountActivity.this, "Erro 400: " + erroDetalhado, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(CreateAccountActivity.this, "Erro 400 Bad Request", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Utilizador> call, Throwable t) {
                Toast.makeText(CreateAccountActivity.this, "Erro de ligação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}