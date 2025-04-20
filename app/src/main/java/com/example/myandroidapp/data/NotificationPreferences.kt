package com.example.myandroidapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define extension at top level
val Context.notificationDataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_settings")

// Use object (singleton) to avoid multiple instances
class NotificationPreferences {

    private val NOTIF_KEY = booleanPreferencesKey("notif_enabled")

    fun isEnabledFlow(context: Context): Flow<Boolean> {
        return context.notificationDataStore.data.map { prefs ->
            prefs[NOTIF_KEY] ?: true
        }
    }

    suspend fun setEnabled(context: Context, enabled: Boolean) {
        context.notificationDataStore.edit { prefs ->
            prefs[NOTIF_KEY] = enabled
        }
    }
}
