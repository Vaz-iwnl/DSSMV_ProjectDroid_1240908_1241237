package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Aula;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuInstrutorActivity extends AppCompatActivity {

    private RecyclerView rvAulasInstrutor;
    private AulasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_instrutor);

        rvAulasInstrutor = findViewById(R.id.rvAulasInstrutor);
        rvAulasInstrutor.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AulasAdapter(aulaClicada -> {
            Intent intent = new Intent(MenuInstrutorActivity.this, DetalhesAulaActivity.class);
            intent.putExtra("ID_AULA", aulaClicada.getId());
            intent.putExtra("MODALIDADE", aulaClicada.getModalidade());
            intent.putExtra("DATA_AULA", aulaClicada.getData());
            startActivity(intent);
        });

        adapter.setModoInstrutor(true);
        rvAulasInstrutor.setAdapter(adapter);

        buscarAulasDoInstrutor();
    }

    private void buscarAulasDoInstrutor() {
        android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String nomeProfessor = prefs.getString("user_nome", "");

        if (nomeProfessor.isEmpty()) {
            Toast.makeText(this, "Erro: Nome do professor não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }
        GymApiService apiService = ApiClient.getApiService();

        String query = "{\"professor\": \"" + nomeProfessor + "\"}";

        apiService.getAulasPorData(query).enqueue(new Callback<List<Aula>>() {
            @Override
            public void onResponse(Call<List<Aula>> call, Response<List<Aula>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Aula> aulasOriginais = response.body();
                    List<Aula> listaExpandida = new ArrayList<>();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());

                    // Para cada aula base do instrutor, criamos repetições para as próximas 4 semanas
                    for (Aula aulaBase : aulasOriginais) {
                        int diaAlvo = obterDiaDaSemanaAlvo(aulaBase.getDiaSemana());

                        if (diaAlvo != -1) {
                            // Gera as próximas 4 ocorrências desse dia da semana
                            Calendar calendar = Calendar.getInstance();

                            // Avança até encontrar o próximo dia da semana correspondente
                            while (calendar.get(Calendar.DAY_OF_WEEK) != diaAlvo) {
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }

                            for (int i = 0; i < 4; i++) {
                                String dataCalculada = sdf.format(calendar.getTime());

                                Aula aulaCiclo = new Aula();
                                aulaCiclo.setId(aulaBase.getId());
                                aulaCiclo.setModalidade(aulaBase.getModalidade() + " (" + dataCalculada + ")");
                                aulaCiclo.setProfessor(aulaBase.getProfessor());
                                aulaCiclo.setDiaSemana(aulaBase.getDiaSemana());
                                aulaCiclo.setData(dataCalculada); // Guarda a data exata!
                                aulaCiclo.setHora(aulaBase.getHora());
                                aulaCiclo.setDuracao(aulaBase.getDuracao());
                                aulaCiclo.setLotacao(aulaBase.getLotacao());

                                listaExpandida.add(aulaCiclo);

                                calendar.add(Calendar.DAY_OF_MONTH, 7);
                            }
                        }
                    }

                    adapter.setAulas(listaExpandida);

                    if (listaExpandida.isEmpty()) {
                        Toast.makeText(MenuInstrutorActivity.this, "Ainda não tens aulas atribuídas.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MenuInstrutorActivity.this, "Erro a carregar aulas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Aula>> call, Throwable t) {
                Toast.makeText(MenuInstrutorActivity.this, "Falha na net.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obterDiaDaSemanaAlvo(String diaStr) {
        if (diaStr == null) return -1;
        switch (diaStr.toLowerCase()) {
            case "domingo": return Calendar.SUNDAY;
            case "segunda": return Calendar.MONDAY;
            case "terça": case "terca": return Calendar.TUESDAY;
            case "quarta": return Calendar.WEDNESDAY;
            case "quinta": return Calendar.THURSDAY;
            case "sexta": return Calendar.FRIDAY;
            case "sábado": case "sabado": return Calendar.SATURDAY;
            default: return -1;
        }
    }
}