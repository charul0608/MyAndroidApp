package com.example.myandroidapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myandroidapp.data.db.ApiObjectEntity
import com.example.myandroidapp.data.repository.ApiRepository
import com.example.myandroidapp.util.NotificationPreferenceManager
import kotlinx.coroutines.launch

class ApiViewModel(private val repository: ApiRepository) : ViewModel() {

    private val _apiObjects = MutableLiveData<List<ApiObjectEntity>>()
    val apiObjects: LiveData<List<ApiObjectEntity>> get() = _apiObjects

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchObjects() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val success = repository.fetchAndStoreObjects()
                if (success) {
                    _apiObjects.value = repository.getAllObjects()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to fetch data."
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateObject(entity: ApiObjectEntity) {
        viewModelScope.launch {
            repository.updateObject(entity)
            fetchObjects()
        }
    }

    fun deleteObject(entity: ApiObjectEntity) {
        viewModelScope.launch {
            repository.deleteObject(entity)
            fetchObjects()
        }
    }

    fun deleteObject(entity: ApiObjectEntity, context: Context) {
        viewModelScope.launch {
            repository.deleteObject(entity)
            fetchObjects()

            val prefs = NotificationPreferenceManager(context)
            prefs.notificationsEnabled.collect { isEnabled ->
                if (isEnabled) {
                    // Here we would normally call a cloud function or backend to send FCM message
                    Log.d("FCM", "FCM would send notification: Deleted ${entity.name}")
                }
            }
        }
    }
}