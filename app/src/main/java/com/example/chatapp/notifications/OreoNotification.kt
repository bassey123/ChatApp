package com.example.chatapp.notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build


class OreoNotification(base: Context) : ContextWrapper(base) {

    val CHANNEL_ID = "com.example.chatapp"
    val CHANNEL_NAME = "chatapp"

    lateinit var notificationManager: NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {

        val channel = NotificationChannel(CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(false)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        getManager().createNotificationChannel(channel)
    }

    fun getManager() : NotificationManager {
        if (notificationManager == null) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return  notificationManager
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun getOreoNotification(title: String, body: String,
                            pendingIntent: PendingIntent, soundUri: Uri, icon: String): Notification.Builder {
        return Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(icon.toInt())
            .setSound(soundUri)
            .setAutoCancel(true)
    }

}
