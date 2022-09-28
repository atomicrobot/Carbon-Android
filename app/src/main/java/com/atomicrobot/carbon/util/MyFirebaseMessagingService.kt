package com.atomicrobot.carbon.util

import com.atomicrobot.carbon.notification.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // Note: Copy the token from logcat and paste into Firebase Cloud Messaging console to
        // target this device directly
        Timber.i("Refreshed token: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.i("From: ${remoteMessage.from}")
        Timber.i("Notification Message Body: ${remoteMessage.notification!!.body}")

        Notification().sendNotification(this, remoteMessage.notification!!.title, remoteMessage.notification!!.body)
    }
}
