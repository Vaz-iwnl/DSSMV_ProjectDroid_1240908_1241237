package com.example.dssmv_projectdroid_1240908_1241237.ui.main;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;
import com.example.dssmv_projectdroid_1240908_1241237.network.ApiClient;
import com.example.dssmv_projectdroid_1240908_1241237.network.GymApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testarConexaoRestDB();
    }

    private void testarConexaoRestDB() {
        GymApiService apiService = ApiClient.getApiService();

        // Executa o pedido GET para ir buscar a lista de desportos/cards
        apiService.getDesportos().enqueue(new Callback<List<Desporto>>() {
            @Override
            public void onResponse(Call<List<Desporto>> call, Response<List<Desporto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Desporto> lista = response.body();
                    Log.d("RESTDB_SUCESSO", "Recebidos " + lista.size() + " registos!");
                    Toast.makeText(MainActivity.this, "Conectado ao RestDB! Total: " + lista.size(), Toast.LENGTH_LONG).show();
                } else {
                    Log.e("RESTDB_ERRO", "Erro da API. Código HTTP: " + response.code());
                    Toast.makeText(MainActivity.this, "Erro da API: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Desporto>> call, Throwable t) {
                Log.e("RESTDB_FALHA", "Falha de rede: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Falha de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}