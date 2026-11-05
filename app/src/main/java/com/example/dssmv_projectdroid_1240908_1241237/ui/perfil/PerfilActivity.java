package com.example.dssmv_projectdroid_1240908_1241237.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.ui.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private EditText etNomePerfil, etEmailPerfil, etNovaPassword, etConfirmarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar os campos
        etNomePerfil = findViewById(R.id.etNomePerfil);
        etEmailPerfil = findViewById(R.id.etEmailPerfil);
        etNovaPassword = findViewById(R.id.etNovaPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);

        Button btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        Button btnLogout = findViewById(R.id.btnLogout);

        carregarDadosAtuais();

        // Clique em Guardar
        btnGuardarPerfil.setOnClickListener(v -> guardarAlteracoes());

        // Clique em Logout
        btnLogout.setOnClickListener(v -> efetuarLogout());
    }

    private void carregarDadosAtuais() {
        // Lê os dados que foram guardados na SharedPreferences no momento do Login
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String nome = preferences.getString("user_nome", "");
        String email = preferences.getString("user_email", "");

        etNomePerfil.setText(nome);
        etEmailPerfil.setText(email);
    }

    private void guardarAlteracoes() {
        String novoNome = etNomePerfil.getText().toString().trim();
        String novoEmail = etEmailPerfil.getText().toString().trim();
        String novaPass = etNovaPassword.getText().toString().trim();
        String confPass = etConfirmarPassword.getText().toString().trim();

        if (novoNome.isEmpty() || novoEmail.isEmpty()) {
            Toast.makeText(this, "Nome e Email são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação da Password (apenas verifica se coincidem)
        if (!novaPass.isEmpty()) {
            if (!novaPass.equals(confPass)) {
                Toast.makeText(this, "As passwords não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 1. Obter o ID do utilizador logado
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String userId = preferences.getString("user_id", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "Erro: ID de utilizador não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Criar o objeto Utilizador com os novos dados
        Utilizador utilizadorAtualizado = new Utilizador();
        utilizadorAtualizado.setNome(novoNome);
        utilizadorAtualizado.setEmail(novoEmail);

        if (!novaPass.isEmpty()) {
            utilizadorAtualizado.setPassword(novaPass);
        }

        // 3. Enviar para a API
        ApiClient.getApiService().atualizarUtilizador(userId, utilizadorAtualizado).enqueue(new Callback<Utilizador>() {
            @Override
            public void onResponse(Call<Utilizador> call, Response<Utilizador> response) {
                if (response.isSuccessful()) {
                    // Atualizar os dados locais para manter a sessão correta
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_nome", novoNome);
                    editor.putString("user_email", novoEmail);
                    editor.apply();

                    Toast.makeText(PerfilActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    etNovaPassword.setText("");
                    etConfirmarPassword.setText("");
                } else {
                    Toast.makeText(PerfilActivity.this, "Erro ao atualizar na base de dados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilizador> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Falha de rede ao tentar atualizar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void efetuarLogout() {
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}