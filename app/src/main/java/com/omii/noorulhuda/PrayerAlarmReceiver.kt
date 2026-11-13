package com.omii.noorulhuda

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import org.json.JSONArray
import java.util.Calendar

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayer = intent.getStringExtra("prayer_name") ?: "Prayer time"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "noor_prayer_alerts"
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "Prayer time reminders", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "A gentle notification tone at each prayer time"
            channel.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI, null)
            manager.createNotificationChannel(channel)
        }
        val builder = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(context, channelId) else Notification.Builder(context)
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Noor-ul-Huda • Prayer time")
            .setContentText("$prayer time has started")
            .setAutoCancel(true)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setPriority(Notification.PRIORITY_HIGH)
        manager.notify(intent.getIntExtra("notification_id", 1001), builder.build())
    }

    companion object {
        private const val FIRST_ID = 1001
        fun scheduleAll(context: Context, json: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            cancelAll(context)
            val times = JSONArray(json)
            for (i in 0 until times.length()) {
                val item = times.getJSONObject(i)
                val name = item.optString("name", "Prayer")
                val value = item.optString("time", "")
                val parts = value.split(":")
                if (parts.size < 2) continue
                val hour = parts[0].toIntOrNull() ?: continue
                val minute = parts[1].take(2).toIntOrNull() ?: continue
                if (hour !in 0..23 || minute !in 0..59) continue
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
                }
                val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                    putExtra("prayer_name", name)
                    putExtra("notification_id", FIRST_ID + i)
                }
                val pending = PendingIntent.getBroadcast(context, FIRST_ID + i, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0)
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pending)
            }
        }

        fun cancelAll(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            for (i in 0 until 5) {
                val intent = Intent(context, PrayerAlarmReceiver::class.java)
                val pending = PendingIntent.getBroadcast(context, FIRST_ID + i, intent,
                    PendingIntent.FLAG_NO_CREATE or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0)
                if (pending != null) {
                    alarmManager.cancel(pending)
                    pending.cancel()
                }
            }
        }
    }
}

class PrayerBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("prayer_alerts", Context.MODE_PRIVATE)
            if (prefs.getBoolean("enabled", false)) {
                PrayerAlarmReceiver.scheduleAll(context, prefs.getString("times", "[]") ?: "[]")
            }
        }
    }
}
