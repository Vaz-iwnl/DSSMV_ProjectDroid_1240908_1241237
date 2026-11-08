package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Aula;
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
    private String dataAula;
    private Aula aulaBaseAtual;
    private EditText etLotacao;
    private Button btnAtualizarLotacao;
    private ListView listViewAlunos;
    private List<Inscricao> listaInscricoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_aula);

        idAula = getIntent().getStringExtra("ID_AULA");
        modalidade = getIntent().getStringExtra("MODALIDADE");
        dataAula = getIntent().getStringExtra("DATA_AULA");

        TextView tvTituloAula = findViewById(R.id.tvTituloAula);
        tvTituloAula.setText(modalidade != null ? modalidade : "Detalhes da Aula");

        listViewAlunos = findViewById(R.id.listViewAlunos);
        Button btnCancelarAula = findViewById(R.id.btnCancelarAula);

        buscarAlunosInscritos();

        btnCancelarAula.setOnClickListener(v -> mostrarDialogoConfirmacao());

        etLotacao = findViewById(R.id.etLotacao);
        btnAtualizarLotacao = findViewById(R.id.btnAtualizarLotacao);

        btnAtualizarLotacao.setOnClickListener(v -> atualizarLotacao());

        buscarDadosDaAula();
    }

    private void buscarAlunosInscritos() {
        GymApiService apiService = ApiClient.getApiService();
        String query = "{\"id_aula\":\"" + idAula + "\", \"data_aula\":\"" + dataAula + "\"}";

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

        for (Inscricao insc : listaInscricoes) {
            apiService.apagarInscricao(insc.get_id()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> r) {}
                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });
        }
        Inscricao aulaCancelada = new Inscricao(idAula, "AULA_CANCELADA", dataAula, modalidade);
        apiService.inscreverAula(aulaCancelada).enqueue(new Callback<Inscricao>() {
            @Override
            public void onResponse(Call<Inscricao> call, Response<Inscricao> response) {
                Toast.makeText(DetalhesAulaActivity.this, "Aula cancelada para este dia!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Inscricao> call, Throwable t) {
                Toast.makeText(DetalhesAulaActivity.this, "Erro de rede.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void buscarDadosDaAula() {
        GymApiService apiService = ApiClient.getApiService();
        String query = "{\"_id\":\"" + idAula + "\"}";

        apiService.getAulasPorData(query).enqueue(new Callback<List<Aula>>() {
            @Override
            public void onResponse(Call<List<Aula>> call, Response<List<Aula>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    aulaBaseAtual = response.body().get(0);
                    etLotacao.setText(String.valueOf(aulaBaseAtual.getLotacao()));
                }
            }

            @Override
            public void onFailure(Call<List<Aula>> call, Throwable t) {
                Toast.makeText(DetalhesAulaActivity.this, "Erro a carregar a lotação.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarLotacao() {
        if (aulaBaseAtual == null) {
            Toast.makeText(this, "A aguardar dados da aula...", Toast.LENGTH_SHORT).show();
            return;
        }

        String lotacaoStr = etLotacao.getText().toString().trim();
        if (lotacaoStr.isEmpty()) {
            Toast.makeText(this, "Insere um número válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        int novaLotacao = Integer.parseInt(lotacaoStr);
        if (novaLotacao <= 0) {
            Toast.makeText(this, "A lotação tem de ser maior que 0.", Toast.LENGTH_SHORT).show();
            return;
        }

        aulaBaseAtual.setLotacao(novaLotacao);

        GymApiService apiService = ApiClient.getApiService();
        apiService.atualizarAula(idAula, aulaBaseAtual).enqueue(new Callback<Aula>() {
            @Override
            public void onResponse(Call<Aula> call, Response<Aula> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalhesAulaActivity.this, "Lotação atualizada com sucesso para todas as semanas!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetalhesAulaActivity.this, "Erro ao atualizar a aula.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Aula> call, Throwable t) {
                Toast.makeText(DetalhesAulaActivity.this, "Falha de rede ao atualizar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}