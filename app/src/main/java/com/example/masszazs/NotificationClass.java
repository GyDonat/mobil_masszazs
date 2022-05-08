package com.example.masszazs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationClass {

    private NotificationManager manager;
    private Context context;
    private static final String CHANNEL_ID = "notify";
    private final int notID = 420;
    private static int appointments_key = 5432345;

    public NotificationClass(Context context) {
        this.context = context;
        this.manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }


    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Értesítés neve", importance);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setDescription("helo szia description"/*description*/);
            this.manager.createNotificationChannel(channel);
        }
    }

    public void notify(String szoveg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_calendar)
                .setContentTitle("Sikeres foglalás!")
                .setContentText("Foglalt időpont: " + szoveg);
        this.manager.notify(notID, builder.build());

    }

}
