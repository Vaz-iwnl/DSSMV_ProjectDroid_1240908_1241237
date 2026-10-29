package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Aula;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;
import com.example.dssmv_projectdroid_1240908_1241237.ui.main.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView rvAulasDia;
    private AulasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        calendarView = findViewById(R.id.calendarView);
        rvAulasDia = findViewById(R.id.rvAulasDia);

        rvAulasDia.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AulasAdapter();
        rvAulasDia.setAdapter(adapter);

        ImageView icHome = findViewById(R.id.icHome);
        if (icHome != null) {
            icHome.setOnClickListener(v -> {
                Intent intent = new Intent(AgendamentoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                int diaDaSemanaNumero = cal.get(java.util.Calendar.DAY_OF_WEEK);

                String[] diasEmTexto = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
                String diaSelecionado = diasEmTexto[diaDaSemanaNumero - 1];

                buscarAulasNaApi(diaSelecionado);
            }
        });
    }

    private void buscarAulasNaApi(String dia) {
        GymApiService apiService = ApiClient.getApiService();

        String query = "{\"dia_semana\": \"" + dia + "\"}";

        apiService.getAulasPorData(query).enqueue(new Callback<List<Aula>>() {
            @Override
            public void onResponse(Call<List<Aula>> call, Response<List<Aula>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Aula> aulas = response.body();
                    adapter.setAulas(aulas);

                    if (aulas.isEmpty()) {
                        Toast.makeText(AgendamentoActivity.this, "Sem aulas marcadas para este dia.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AgendamentoActivity.this, "Erro da API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Aula>> call, Throwable t) {
                Toast.makeText(AgendamentoActivity.this, "Falha de rede: Verifica a net.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}