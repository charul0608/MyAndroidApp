package com.example.myandroidapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myandroidapp.data.db.ApiObjectEntity
import com.example.myandroidapp.data.repository.ApiRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

import com.example.myandroidapp.data.NotificationPreferences
import com.example.myandroidapp.data.api.ApiClient
import com.example.myandroidapp.data.api.ApiService
import com.example.myandroidapp.data.db.ApiObjectDto
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONException


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
            val rowsAffected = repository.updateObject(entity)
            Log.d("UPDATE", "Rows updated: $rowsAffected")

            repository.updateObject(entity)
            fetchObjects()
        }

    }

    private fun sendDeleteNotification(context: Context, token: String, itemName: String) {
        val url = "https://<your-cloud-function-url>" // Replace with your deployed function URL
        val jsonBody = JSONObject().apply {
            put("token", token)
            put("name", itemName)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { Log.d("Notification", "Notification sent") },
            { error -> Log.e("Notification", "Error", error) }
        )

        Volley.newRequestQueue(context).add(request)
    }



    private fun deleteItemAndNotify(
        context: Context,
        item: ApiObjectDto,
        token: String,
        prefs: NotificationPreferences,
        apiService: ApiService // you don't need to pass ApiService, as you can use ApiClient
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = ApiClient.deleteItem(item.id)
                if (response.isSuccessful) {
                    prefs.isEnabled.firstOrNull()?.let { isEnabled ->
                        if (isEnabled) {
                            sendDeleteNotification(context, token, item.name)
                        }
                    }
                } else {
                    Log.e("API", "Failed to delete")
                }
            } catch (e: Exception) {
                Log.e("API", "Error deleting item", e)
            }
        }
    }


    fun deleteTestObject(context: Context, token: String, prefs: NotificationPreferences) {
        val id = "ff808181932badb601964e0576fb2474"
        val name = "Test Device" // Name you used while creating

        val dto = ApiObjectDto(id = id, name = name, data = null)

        deleteItemAndNotify(
            context = context,
            item = dto,
            token = token,
            prefs = prefs,
            apiService = ApiClient.retrofit
        )
    }



//
//    fun updateObject(entity: ApiObjectEntity) {
//        viewModelScope.launch {
//            val rowsAffected = repository.updateObject(entity)
//            Log.d("UPDATE", "Rows updated: $rowsAffected")
//
//            if (rowsAffected > 0) {
//                fetchObjects()
//            } else {
//                _errorMessage.value = "Update failed: No rows affected"
//            }
//        }
//    }


//    fun deleteObject(entity: ApiObjectEntity) {
//        viewModelScope.launch {
//            repository.deleteObject(entity)
//            fetchObjects()
//        }
//    }
//
//    fun deleteObject(entity: ApiObjectEntity) {
//        viewModelScope.launch {
//            val rowsDeleted = repository.deleteObject(entity)
//            Log.d("DELETE", "Rows deleted: $rowsDeleted")
//
//            if (rowsDeleted > 0) {
//                fetchObjects()
//            } else {
//                _errorMessage.value = "Delete failed: No rows affected"
//            }
//        }
//    }

    fun entityToDto(entity: ApiObjectEntity): ApiObjectDto {
        val map: Map<String, Any>? = try {
            val jsonObject = JSONObject(entity.data)
            jsonObject.keys().asSequence().associateWith { key ->
                jsonObject.get(key)
            }
        } catch (e: JSONException) {
            null
        }

        return ApiObjectDto(
            id = entity.id,
            name = entity.name,
            data = map
        )
    }


    fun deleteObject(
        entity: ApiObjectEntity,
        context: Context,
        token: String,
        prefs: NotificationPreferences,
        apiService: ApiService
    ) {
        viewModelScope.launch {
            try {
                // Step 1: Delete the object from the repository
                val rowsDeleted = repository.deleteObject(entity)
                Log.d("DELETE", "Rows deleted: $rowsDeleted")

                // Step 2: Check if the deletion was successful
                if (rowsDeleted > 0) {
                    // Fetch the latest list of objects (optional, depending on your use case)
                    fetchObjects()

                    // Step 3: Convert the ApiObjectEntity to ApiObjectDto (the DTO for API)
                    val dto = entityToDto(entity)

                    // Step 4: Call deleteItemAndNotify to notify about the deletion
                    deleteItemAndNotify(
                        context = context,
                        item = dto,  // pass the DTO object
                        token = token,  // pass the token for notification
                        prefs = prefs,  // pass the preferences to check notification setting
                        apiService = apiService  // pass the ApiService to make API calls
                    )
                } else {
                    // If no rows were deleted, set an error message
                    _errorMessage.value = "Delete failed: No rows affected"
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during deletion
                _errorMessage.value = "Error deleting object: ${e.message}"
                Log.e("DELETE", "Error deleting object", e)
            }
        }
    }



//    fun deleteObject(entity: ApiObjectEntity, context: Context) {
//        viewModelScope.launch {
//
//            val rowsDeleted = repository.deleteObject(entity)
//            Log.d("DELETE", "Rows deleted: $rowsDeleted")
//
//            repository.deleteObject(entity)
//            fetchObjects()
//
//            val prefs = NotificationPreferenceManager(context)
//            prefs.notificationsEnabled.collect { isEnabled ->
//                if (isEnabled) {
//                    // Here we would normally call a cloud function or backend to send FCM message
//                    Log.d("FCM", "FCM would send notification: Deleted ${entity.name}")
//                }
//            }
//
//        }
//    }

}