package com.example.myandroidapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapp.R
import com.example.myandroidapp.data.db.ApiObjectEntity

class ApiObjectAdapter(
    private val onDelete: (ApiObjectEntity) -> Unit,
    private val onUpdate: (ApiObjectEntity) -> Unit
) : ListAdapter<ApiObjectEntity, ApiObjectAdapter.ApiViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ApiObjectEntity>() {
        override fun areItemsTheSame(oldItem: ApiObjectEntity, newItem: ApiObjectEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiObjectEntity, newItem: ApiObjectEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_api_object, parent, false)
        return ApiViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ApiObjectEntity) {
            itemView.findViewById<TextView>(R.id.textViewName).text = item.name
            itemView.findViewById<TextView>(R.id.textViewId).text = item.data

            itemView.findViewById<Button>(R.id.btnDelete).setOnClickListener { onDelete(item) }
            itemView.findViewById<Button>(R.id.btnUpdate).setOnClickListener { onUpdate(item) }
        }
    }
}