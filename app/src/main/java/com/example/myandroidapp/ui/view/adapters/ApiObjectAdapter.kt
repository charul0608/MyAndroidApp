package com.example.myandroidapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapp.data.db.ApiObjectEntity
import com.example.myandroidapp.databinding.ItemApiObjectBinding
import com.example.myandroidapp.ui.view.ApiObjectDiffCallback

class ApiObjectAdapter(
    private val onDelete: (ApiObjectEntity) -> Unit,
    private val onUpdate: (ApiObjectEntity) -> Unit
) : ListAdapter<ApiObjectEntity, ApiObjectAdapter.ApiObjectViewHolder>(ApiObjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiObjectViewHolder {
        val binding = ItemApiObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApiObjectViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ApiObjectViewHolder(private val binding: ItemApiObjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApiObjectEntity) {
            binding.textViewName.text = item.name
            binding.textViewId.text = item.data

            binding.btnDelete.setOnClickListener {
                onDelete(item)
            }

            binding.btnUpdate.setOnClickListener {
                onUpdate(item)
            }
        }
    }
}
