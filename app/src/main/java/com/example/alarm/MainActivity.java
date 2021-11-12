package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Button button = findViewById(R.id.button);
        TextView text = findViewById(R.id.text);
        text.setText("");

        button.setOnClickListener(view -> {
            text.setText("Будильник установлен");

            // PendingIntent - контейнер для Intent. Наш intent запустит AlarmReceiver
            // Подробнее можно почитать тут: https://startandroid.ru/ru/uroki/vse-uroki-spiskom/204-urok-119-pendingintent-flagi-requestcode-alarmmanager.html
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long currentTime = System.currentTimeMillis(); // этот метод для получения текущего времени можно использовать не только в Android
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + 10 * 1000, pendingIntent);

        });
    }

    private void createNotificationChannel() {
        // NotificationChannel (канал уведомления) необходимо создавать если версия API 26 или больше.
        // Канал уведомления позволяет пользователю по-разному настраивать уведомления разных типов от одного приложения
        // Например, можно сделать канал для уведомлений-будильников и уведоблений о том, что версия приложения устарела
        // И пользователь может выбрать, что некоторые уведомления будут приходить со звуком, а некоторые только с вибрацией
        // Подробнее можно почитать тут (по-русски): https://startandroid.ru/ru/uroki/vse-uroki-spiskom/515-urok-190-notifications-kanaly.html

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // id канала
            String channelId = "alarmChannel";
            // Имя канала - видно пользователю
            String channelName = "Alarm Channel";
            // Описание канала - видно пользователю
            String channelDescription = "Channel for alarms";
            // Важность канала
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}