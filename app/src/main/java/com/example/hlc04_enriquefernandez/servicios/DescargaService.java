package com.example.hlc04_enriquefernandez.servicios;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DescargaService extends IntentService {
    // URL de la imagen a descargar
    private static final String URL = "http://www.thebiblescholar.com/android_awesome.jpg";

    public DescargaService() {
        super("DescargaService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Descargamos la imagen como array de bytes
        byte[] imageData = descargarImagen();
        // Creamos un Intent para notificar la descarga completada
        Intent completeIntent = new Intent(Filtros.DOWNLOAD_COMPLETE);
        // Añadimos los datos de la imagen descargada como extra
        completeIntent.putExtra("imagen", imageData);
        // Enviamos el Intent a través del LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).sendBroadcast(completeIntent);
    }
    // Método para descargar la imagen
    private byte[] descargarImagen() {
        try {
            // Creamos una conexión HTTP a la URL de la imagen
            java.net.URL url = new URL(URL);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoInput(true);
            conexion.connect();
            // Obtenemos el InputStream de la conexión
            InputStream input = conexion.getInputStream();
            // Convertimos el InputStream en un array de bytes
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            // Cerramos el InputStream y la conexión
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
