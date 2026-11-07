package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Inscricao;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesAulaActivity extends AppCompatActivity {

    private String idAula;
    private String modalidade;
    private ListView listViewAlunos;
    private List<Inscricao> listaInscricoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_aula);

        // Receber os dados passados pelo ecrã anterior
        idAula = getIntent().getStringExtra("ID_AULA");
        modalidade = getIntent().getStringExtra("MODALIDADE");

        TextView tvTituloAula = findViewById(R.id.tvTituloAula);
        tvTituloAula.setText(modalidade != null ? modalidade : "Detalhes da Aula");

        listViewAlunos = findViewById(R.id.listViewAlunos);
        Button btnCancelarAula = findViewById(R.id.btnCancelarAula);

        buscarAlunosInscritos();

        btnCancelarAula.setOnClickListener(v -> mostrarDialogoConfirmacao());
    }

    private void buscarAlunosInscritos() {
        GymApiService apiService = ApiClient.getApiService();
        String query = "{\"id_aula\":\"" + idAula + "\"}";

        apiService.verificarInscricao(query).enqueue(new Callback<List<Inscricao>>() {
            @Override
            public void onResponse(Call<List<Inscricao>> call, Response<List<Inscricao>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaInscricoes = response.body();
                    List<String> nomesAlunos = new ArrayList<>();

                    for (Inscricao insc : listaInscricoes) {
                        nomesAlunos.add(insc.getUsername_aluno());
                    }

                    if (nomesAlunos.isEmpty()) {
                        nomesAlunos.add("Sem alunos inscritos ainda.");
                    }

                    // Preencher a ListView de forma simples
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            DetalhesAulaActivity.this,
                            android.R.layout.simple_list_item_1,
                            nomesAlunos
                    );
                    listViewAlunos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Inscricao>> call, Throwable t) {
                Toast.makeText(DetalhesAulaActivity.this, "Erro a carregar alunos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoConfirmacao() {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Aula")
                .setMessage("Tens a certeza que queres cancelar esta aula? Os alunos serão removidos.")
                .setPositiveButton("Sim, Cancelar", (dialog, which) -> cancelarAula())
                .setNegativeButton("Não", null)
                .show();
    }

    private void cancelarAula() {
        GymApiService apiService = ApiClient.getApiService();

        // 1. Apagar a aula
        apiService.apagarAula(idAula).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (Inscricao insc : listaInscricoes) {
                        apiService.apagarInscricao(insc.get_id()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> r) {}
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {}
                        });
                    }

                    Toast.makeText(DetalhesAulaActivity.this, "Aula cancelada com sucesso!", Toast.LENGTH_LONG).show();
                    finish(); // Fecha este ecrã e volta à lista
                } else {
                    Toast.makeText(DetalhesAulaActivity.this, "Erro ao cancelar aula.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalhesAulaActivity.this, "Falha de rede ao cancelar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}