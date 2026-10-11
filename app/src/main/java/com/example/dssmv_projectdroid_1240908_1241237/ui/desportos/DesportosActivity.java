package com.example.dssmv_projectdroid_1240908_1241237.ui.desportos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesportosActivity extends AppCompatActivity {

    private RecyclerView rvDesportos;
    private ProgressBar progressBar;
    private DesportosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desportos);

        rvDesportos = findViewById(R.id.rvDesportos);
        progressBar = findViewById(R.id.progressBar);

        rvDesportos.setLayoutManager(new LinearLayoutManager(this));

        carregarDadosDaApi();
    }

    private void carregarDadosDaApi() {

        progressBar.setVisibility(View.VISIBLE);
        rvDesportos.setVisibility(View.GONE);

        ApiClient.getApiService().getDesportos().enqueue(new Callback<List<Desporto>>() {
            @Override
            public void onResponse(Call<List<Desporto>> call, Response<List<Desporto>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Desporto> lista = response.body();

                    adapter = new DesportosAdapter(lista);
                    rvDesportos.setAdapter(adapter);

                    rvDesportos.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DesportosActivity.this, "Erro da API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Desporto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DesportosActivity.this, "Falha de rede.", Toast.LENGTH_SHORT).show();
                Log.e("API_ERRO", "Erro de rede: ", t);
            }
        });
    }
}