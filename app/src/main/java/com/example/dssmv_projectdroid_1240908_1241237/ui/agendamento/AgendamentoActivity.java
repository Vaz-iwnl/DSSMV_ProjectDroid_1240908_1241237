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
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Inscricao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView rvAulasDia;
    private AulasAdapter adapter;
    private java.util.Calendar dataSelecionada = java.util.Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        calendarView = findViewById(R.id.calendarView);
        rvAulasDia = findViewById(R.id.rvAulasDia);

        rvAulasDia.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AulasAdapter(aulaClicada -> {
            fazerInscricao(aulaClicada);
        });
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
                dataSelecionada.set(year, month, dayOfMonth);

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

    private void fazerInscricao(Aula aula) {
        try {
            if (aula.getHora() != null && aula.getHora().contains(":")) {
                String[] partesHora = aula.getHora().split(":");
                String horaLimpa = partesHora[0].replaceAll("[^0-9]", "");
                String minutoLimpo = partesHora[1].replaceAll("[^0-9]", "");

                java.util.Calendar agora = java.util.Calendar.getInstance();
                java.util.Calendar dataHoraAula = java.util.Calendar.getInstance();

                dataHoraAula.set(java.util.Calendar.YEAR, dataSelecionada.get(java.util.Calendar.YEAR));
                dataHoraAula.set(java.util.Calendar.MONTH, dataSelecionada.get(java.util.Calendar.MONTH));
                dataHoraAula.set(java.util.Calendar.DAY_OF_MONTH, dataSelecionada.get(java.util.Calendar.DAY_OF_MONTH));

                dataHoraAula.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(horaLimpa));
                dataHoraAula.set(java.util.Calendar.MINUTE, Integer.parseInt(minutoLimpo));
                dataHoraAula.set(java.util.Calendar.SECOND, 0);
                dataHoraAula.set(java.util.Calendar.MILLISECOND, 0);

                if (dataHoraAula.getTimeInMillis() < agora.getTimeInMillis()) {
                    Toast.makeText(this, "Esta aula já passou!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Erro: Formato de hora inválido.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao processar a data.", Toast.LENGTH_SHORT).show();
            return;
        }

        String idAula = aula.getId();
        GymApiService apiService = ApiClient.getApiService();

        android.content.SharedPreferences prefs = getSharedPreferences("SessaoGinasio", MODE_PRIVATE);
        String usernameLogado = prefs.getString("USERNAME_ALUNO", "Aluno Desconhecido");

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String dataFormatada = sdf.format(dataSelecionada.getTime());

        String queryVerificacao = "{\"id_aula\":\"" + idAula + "\", \"username_aluno\":\"" + usernameLogado + "\", \"data_aula\":\"" + dataFormatada + "\"}";

        apiService.verificarInscricao(queryVerificacao).enqueue(new Callback<List<Inscricao>>() {
            @Override
            public void onResponse(Call<List<Inscricao>> call, Response<List<Inscricao>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isEmpty()) {
                        Inscricao novaInscricao = new Inscricao(idAula, usernameLogado, dataFormatada);

                        apiService.inscreverAula(novaInscricao).enqueue(new Callback<Inscricao>() {
                            @Override
                            public void onResponse(Call<Inscricao> call, Response<Inscricao> responsePost) {
                                if (responsePost.isSuccessful()) {
                                    Toast.makeText(AgendamentoActivity.this, "Inscrição confirmada!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Inscricao> call, Throwable t) {
                                Toast.makeText(AgendamentoActivity.this, "Falha a inscrever.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(AgendamentoActivity.this, "Já estás inscrito nesta aula!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inscricao>> call, Throwable t) {
                Toast.makeText(AgendamentoActivity.this, "Falha de rede. Tenta de novo.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}