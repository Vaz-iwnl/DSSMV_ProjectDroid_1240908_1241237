package com.example.dssmv_projectdroid_1240908_1241237.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton; // Import necessário para o ImageButton
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.ui.auth.LoginActivity;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // 1. Configuração do Botão de Logout
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> efetuarLogout());

        // 2. Configuração do Botão de Navegação da barra inferior (agora dentro do onCreate)
        ImageButton btnNavPerfil = findViewById(R.id.btnNavPerfil);
        if (btnNavPerfil != null) {
            btnNavPerfil.setOnClickListener(v -> {
                // Abre a PerfilActivity se ainda não estiveres nela
                if (!(this instanceof PerfilActivity)) {
                    Intent intent = new Intent(this, PerfilActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void efetuarLogout() {
        // Apagar dados guardados da sessão local
        SharedPreferences preferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirecionar para o Login e limpar histórico de ecrãs
        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}