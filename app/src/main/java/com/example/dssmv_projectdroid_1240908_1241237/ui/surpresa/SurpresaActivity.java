package com.example.dssmv_projectdroid_1240908_1241237.ui.surpresa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.ui.main.MainActivity;

import java.util.Random;

public class SurpresaActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor acelerometro;
    private TextView tvDesafio;

    private float aceleracaoAtual;
    private float aceleracaoAnterior;
    private long ultimoAbanao;

    private final String[] desafios = {
            "20 Flexões!",
            "1 minuto de prancha!",
            "20 Agachamentos!",
            "30 Abdominais",
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surpresa);

        tvDesafio = findViewById(R.id.tvDesafio);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        aceleracaoAtual = SensorManager.GRAVITY_EARTH;
        aceleracaoAnterior = SensorManager.GRAVITY_EARTH;

        ImageView icHome = findViewById(R.id.icHome);
        if (icHome != null) {
            icHome.setOnClickListener(v -> {
                Intent intent = new Intent(SurpresaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        ImageView btnMinhasAulas = findViewById(R.id.icMinhasAulas);

        if (btnMinhasAulas != null) {
            btnMinhasAulas.setOnClickListener(v -> {
                android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isProfessor = prefs.getBoolean("is_professor", false);

                Intent intent;
                if (isProfessor) {
                    intent = new Intent(SurpresaActivity.this, com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento.MenuInstrutorActivity.class);
                } else {
                    intent = new Intent(SurpresaActivity.this, com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento.MinhasAulasActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }

        ImageView btnPerfil = findViewById(R.id.btnNavPerfil);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> {
                Intent intent = new Intent(SurpresaActivity.this, com.example.dssmv_projectdroid_1240908_1241237.ui.perfil.PerfilActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            aceleracaoAnterior = aceleracaoAtual;

            aceleracaoAtual = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = Math.abs(aceleracaoAtual - aceleracaoAnterior);
            if (delta > 12) {
                long tempoAtual = System.currentTimeMillis();

                if (tempoAtual - ultimoAbanao > 2000) {
                    ultimoAbanao = tempoAtual;
                    sortearDesafio();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void sortearDesafio() {
        Random random = new Random();
        int index = random.nextInt(desafios.length);
        tvDesafio.setText(desafios[index]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            sensorManager.registerListener(sensorListener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }
}