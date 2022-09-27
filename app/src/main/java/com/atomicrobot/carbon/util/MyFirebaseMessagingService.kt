package com.atomicrobot.carbon.util

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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // Note: Copy the token from logcat and paste into Firebase Cloud Messaging console to
        // target this device directly
        Timber.i("Refreshed token: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.i("From: ${remoteMessage.from}")
        Timber.i("Notification Message Body: ${remoteMessage.notification!!.body}")

        sendNotification(remoteMessage.notification!!)
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        Timber.d("sendNotification")
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, this.getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_bell_notification)
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}