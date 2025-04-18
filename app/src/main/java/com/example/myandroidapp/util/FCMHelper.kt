package com.example.myandroidapp.util

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FCMHelper {
    fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic: $topic")
                } else {
                    Log.e("FCM", "Subscription failed: ${task.exception?.message}")
                }
            }
    }

    fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Unsubscribed from topic: $topic")
                } else {
                    Log.e("FCM", "Unsubscription failed: ${task.exception?.message}")
                }
            }
    }
}