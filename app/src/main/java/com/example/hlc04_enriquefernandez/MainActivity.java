package com.example.hlc04_enriquefernandez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.hlc04_enriquefernandez.servicios.DescargaService;
import com.example.hlc04_enriquefernandez.servicios.MusicService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btStartService = findViewById(R.id.btArrancarServicio);
        Button btStopService = findViewById(R.id.btPararServicio);
        Button btDescarga = findViewById(R.id.btServicioDescarga);

        btStartService.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando Reproductor de música", Toast.LENGTH_SHORT).show();
            startService(new Intent(this, MusicService.class));
        });
        btStopService.setOnClickListener(v -> {
            Toast.makeText(this, "Deteniendo Reproductor de música", Toast.LENGTH_SHORT).show();
            stopService(new Intent(this, MusicService.class));
        });
        btDescarga.setOnClickListener(v -> startService(new Intent(this, DescargaService.class)));
    }
}