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
import com.example.hlc04_enriquefernandez.servicios.Filtros;
import com.example.hlc04_enriquefernandez.servicios.MusicService;

public class MainActivity extends AppCompatActivity {
    // Atributos
    private BroadcastReceiver broadcastReceiver;
    private ImageView ivImagenDescargada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicializamos los componentes
        Button btStartService = findViewById(R.id.btArrancarServicio);
        Button btStopService = findViewById(R.id.btPararServicio);
        Button btDescarga = findViewById(R.id.btServicioDescarga);
        ivImagenDescargada = findViewById(R.id.ivImgDescargada);
        // Creamos los evcentos de los botones
        btStartService.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando Reproductor de música", Toast.LENGTH_SHORT).show();
            startService(new Intent(this, MusicService.class));
        });
        btStopService.setOnClickListener(v -> {
            Toast.makeText(this, "Deteniendo Reproductor de música", Toast.LENGTH_SHORT).show();
            stopService(new Intent(this, MusicService.class));
        });
        btDescarga.setOnClickListener(v -> {
            Toast.makeText(this, "Descargando imagen", Toast.LENGTH_SHORT).show();
            ivImagenDescargada.setImageBitmap(null);
            startService(new Intent(this, DescargaService.class));
        });
        // Creamos el broadcastReceiver
        crearBroadcastReceiver();
        // Registramos el broadcastReceiver con el filtro correspondiente
        IntentFilter filter = new IntentFilter();
        filter.addAction(Filtros.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
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
                // Obtenemos la acción del intent
                String action = intent.getAction();
                // Comprobamos si es la acción que nos interesa
                if (action.equals(Filtros.DOWNLOAD_COMPLETE)) {
                    // Obtenemos el array de bytes de la imagen
                    byte[] imagen = intent.getByteArrayExtra("imagen");
                    // Creamos un bitmap a partir del array de bytes
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                    // Mostramos la imagen en una ImageView
                    ivImagenDescargada.setImageBitmap(bitmap);
                    // Mostramos un Toast indicando que la descarga ha finalizado
                    Toast.makeText(MainActivity.this, "Descarga finalizada", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}