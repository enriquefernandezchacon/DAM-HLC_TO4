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

    private static final String URL = "http://http://www.thebiblescholar.com/android_awesome.jpg";
    public static final String DOWNLOAD_COMPLETE = "com.example.hlc04_enriquefernandez.descargaservice.DOWNLOAD_COMPLETE";

    public DescargaService() {
        super("DescargaService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Descargamos la imagen como array de bytes
        byte[] imageData = descargarImagen();
        // Creamos un Intent para notificar la descarga completada
        Intent completeIntent = new Intent(DOWNLOAD_COMPLETE);
        // Añadimos los datos de la imagen descargada como extra
        completeIntent.putExtra("imagen", imageData);
        // Enviamos el Intent como broadcast local
        LocalBroadcastManager.getInstance(this).sendBroadcast(completeIntent);
    }

    private byte[] descargarImagen() {
        try {
            // Descargamos la imagen como InputStream
            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            // Convertimos el InputStream en un array de bytes
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
