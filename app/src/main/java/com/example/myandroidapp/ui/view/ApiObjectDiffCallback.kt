package com.example.myandroidapp.ui.view

import androidx.recyclerview.widget.DiffUtil
import com.example.myandroidapp.data.db.ApiObjectEntity

class ApiObjectDiffCallback : DiffUtil.ItemCallback<ApiObjectEntity>() {
    override fun areItemsTheSame(oldItem: ApiObjectEntity, newItem: ApiObjectEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ApiObjectEntity, newItem: ApiObjectEntity): Boolean {
        return oldItem == newItem
    }
}
