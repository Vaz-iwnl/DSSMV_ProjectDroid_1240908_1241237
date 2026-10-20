package com.example.dssmv_projectdroid_1240908_1241237.ui.main;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;
import com.example.dssmv_projectdroid_1240908_1241237.ui.desportos.DesportosActivity;
import com.example.dssmv_projectdroid_1240908_1241237.ui.surpresa.SurpresaActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatusApi;
    private Button btnTestarApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testarConexaoRestDB();

        MaterialCardView cardShake = findViewById(R.id.cardShake);
        MaterialCardView cardModalidades = findViewById(R.id.cardModalidades);
        MaterialCardView cardCalendario = findViewById(R.id.cardCalendario);

        cardShake.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SurpresaActivity.class);
            startActivity(intent);
        });

        cardModalidades.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DesportosActivity.class);
            startActivity(intent);
        });

        cardCalendario.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "A página de Calendário será implementada em breve!", Toast.LENGTH_SHORT).show();
        });

    }

    private void testarConexaoRestDB() {
        GymApiService apiService = ApiClient.getApiService();

        apiService.getDesportos().enqueue(new Callback<List<Desporto>>() {
            @Override
            public void onResponse(Call<List<Desporto>> call, Response<List<Desporto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Desporto> lista = response.body();
                    Log.d("RESTDB_SUCESSO", "Recebidos " + lista.size() + " registos!");
                    Toast.makeText(MainActivity.this, "Conectado ao RestDB! Total: " + lista.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("RESTDB_ERRO", "Erro da API. Código HTTP: " + response.code());
                    Toast.makeText(MainActivity.this, "Erro da API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Desporto>> call, Throwable t) {
                Log.e("RESTDB_FALHA", "Falha de rede: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Falha de rede. Verifica o Logcat.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}