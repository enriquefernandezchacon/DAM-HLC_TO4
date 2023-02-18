package com.example.hlc04_enriquefernandez.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.hlc04_enriquefernandez.R;

public class MusicService extends Service {
    // CONSTANTES
    private static final String BASE_URL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-";
    private static final String NOTIFICATION_CHANNEL_ID = "MusicPlayerChannel";
    private static final int NOTIFICATION_ID = 1;
    // ATRIBUTOS
    private MediaPlayer mediaPlayer;
    private NotificationManager notificationManager;
    private int cancion;

    @Override
    public void onCreate() {
        super.onCreate();
        // Creamos el canal de notificaciones
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        crearNotificationChannel();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Creamos la URL de la canción aleatoriamente
        cancion = (int) (Math.random() * 17) + 1;
        String url = BASE_URL + cancion + ".mp3";
        // Si existe una canción en reproducción, la paramos
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        // Iniciamos la reproducción de la canción
        // Instaciamos el MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            // Configuramos el MediaPlayer con la url de la canción
            mediaPlayer.setDataSource(url);
            // Preparamos el MediaPlayer y lo iniciamos
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Mostramos la notificación siempre que la canción esté en reproducción
            showNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Devolvemos START_NOT_STICKY para que el servicio no se reinicie automáticamente
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Paramos la reproducción de la canción cuando el servicio se destruye
        mediaPlayer.stop();
    }
    // Método para crear el canal de notificaciones
    private void crearNotificationChannel() {
        // Comprobamos si la versión de Android es Oreo o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Creamos el canal de notificaciones
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Music Player",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // Registramos el canal de notificaciones
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    // Método para mostrar la notificación
    private void showNotification() {
        // Creamos la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_queue_music_24)
                .setContentTitle("Reproduciendo música...")
                .setContentText("Canción " + cancion + " de 17")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true);
        // Mostramos la notificación
        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
