package com.example.myandroidapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "com.eazyalgo.fcmpushnotification"

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Device FCM token: $token")
        // Send this token to your backend/server if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val builder = NotificationCompat.Builder(this, "fcm_default_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title ?: "FCM Message")
            .setContentText(message ?: "You have a new notification.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationManager.notify(1001, builder.build())

    }

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//
//        if (remoteMessage.getNotification() != null) {
//            generateNotification(
//                remoteMessage.notification!!.title!!,
//                remoteMessage.notification!!.body!!
//            )
//        }
//
//    }
//
//
//    private fun getRemoteView(title: String, message: String): RemoteViews{
//
//        val remoteView = RemoteViews("com.eazyalgo.fcmpushnotification", R.layout.notification)
//
//        remoteView.setTextViewText(R.id.title, title)
//        remoteView.setTextViewText(R.id.message, message)
//        remoteView.setImageViewResource(R.id.app_logo, R.drawable.user_authentication)
//
//        return  remoteView
//    }
//
//
//    private fun generateNotification(title: String, message: String) {
//
//        val intent = Intent(this, MainActivity::class.java)
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
////        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//
//        var builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(applicationContext, channelId)
//                .setSmallIcon(R.drawable.user_authentication)
//                .setAutoCancel(true)
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(title, message))
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//            val notificationChannel =
//                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        notificationManager.notify(0, builder.build())
//    }
}
