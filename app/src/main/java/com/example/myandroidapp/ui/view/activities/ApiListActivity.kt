package com.example.myandroidapp.ui.view.activities

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapp.R
import com.example.myandroidapp.data.db.ApiObjectEntity
import com.example.myandroidapp.data.db.AppDatabase
import com.example.myandroidapp.data.repository.ApiRepository
import com.example.myandroidapp.ui.view.adapters.ApiObjectAdapter
import com.example.myandroidapp.ui.viewmodel.ApiViewModel
import com.example.myandroidapp.ui.viewmodel.ApiViewModelFactory
import com.example.myandroidapp.util.NotificationPreferenceManager
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle



class ApiListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApiObjectAdapter

    private val viewModel: ApiViewModel by viewModels {
        val dao = AppDatabase.getDatabase(this).apiObjectDao()
        ApiViewModelFactory(ApiRepository(dao))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_list)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = ApiObjectAdapter(
            onDelete = { item -> viewModel.deleteObject(item, this@ApiListActivity) }, // ✅ pass context!
            onUpdate = { item -> updateItem(item) }
        )


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.apiObjects.observe(this) {
            adapter.submitList(it)
        }

        viewModel.errorMessage.observe(this) {
            it?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.isLoading.observe(this) {
            findViewById<View>(R.id.progressBar).visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.fetchObjects()

        val switch = findViewById<Switch>(R.id.switchNotific)
        val prefs = NotificationPreferenceManager(this)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                prefs.notificationsEnabled.collect { enabled ->
                    switch.isChecked = enabled
                }
            }
        }


        switch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                prefs.setNotificationsEnabled(isChecked)
            }
        }
    }

    private fun updateItem(item: ApiObjectEntity) {
        val updated = item.copy(name = item.name + " Updated")
        viewModel.updateObject(updated)
    }
}