package com.example.hlc04_enriquefernandez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hlc04_enriquefernandez.servicios.DescargaService;
import com.example.hlc04_enriquefernandez.servicios.MusicService;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

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
        
        crearBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(DescargaService.DOWNLOAD_COMPLETE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistramos el receptor de broadcast
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void crearBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(DescargaService.DOWNLOAD_COMPLETE)) {
                    byte[] imagen = intent.getByteArrayExtra("imagen");
                    // Creamos un bitmap a partir del array de bytes
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                    // Mostramos la imagen en una ImageView
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(MainActivity.this, "Descarga finalizada", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}