package com.atomicrobot.carbon.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.StartActivity
import timber.log.Timber

class Notification {
    fun sendNotification(context: Context, title: String?, body: String?) {
        Timber.d("sendNotification")
        val intent = Intent(context, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.default_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_bell_notification)
            .setColor(ContextCompat.getColor(context.applicationContext, R.color.colorAccent))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
