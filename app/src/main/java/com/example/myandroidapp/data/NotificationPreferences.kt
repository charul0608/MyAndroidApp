package com.example.myandroidapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NotificationPreferences(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("notification_settings")
    private val dataStore = context.dataStore
    private val NOTIF_KEY = booleanPreferencesKey("notif_enabled")

    val isEnabled: Flow<Boolean> = dataStore.data.map { it[NOTIF_KEY] ?: true }

    suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIF_KEY] = enabled }
    }
}
