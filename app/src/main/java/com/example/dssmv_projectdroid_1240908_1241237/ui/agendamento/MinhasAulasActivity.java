package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Inscricao;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;
import com.example.dssmv_projectdroid_1240908_1241237.ui.main.MainActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MinhasAulasActivity extends AppCompatActivity {

    private RecyclerView rvMinhasAulas;
    private MinhasAulasAdapter adapter;
    private GymApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_aulas);

        apiService = ApiClient.getApiService();
        rvMinhasAulas = findViewById(R.id.rvMinhasAulas);
        rvMinhasAulas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MinhasAulasAdapter(inscricao -> apagarInscricao(inscricao));
        rvMinhasAulas.setAdapter(adapter);

        ImageView icHome = findViewById(R.id.icHome);
        if (icHome != null) {
            icHome.setOnClickListener(v -> {
                Intent intent = new Intent(MinhasAulasActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        carregarMinhasAulas();
    }

    private void carregarMinhasAulas() {
        android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String usernameLogado = prefs.getString("user_nome", "");

        String query = "{\"username_aluno\":\"" + usernameLogado + "\"}";

        apiService.verificarInscricao(query).enqueue(new Callback<List<Inscricao>>() {
            @Override
            public void onResponse(Call<List<Inscricao>> call, Response<List<Inscricao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setInscricoes(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Inscricao>> call, Throwable t) {
                Toast.makeText(MinhasAulasActivity.this, "Erro de rede.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apagarInscricao(Inscricao inscricao) {
        apiService.apagarInscricao(inscricao.get_id()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MinhasAulasActivity.this, "Inscrição cancelada!", Toast.LENGTH_SHORT).show();
                    carregarMinhasAulas();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MinhasAulasActivity.this, "Falha ao apagar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}