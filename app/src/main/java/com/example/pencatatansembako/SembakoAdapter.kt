package com.example.pencatatansembako

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pencatatansembako.databinding.ItemSembakoBinding

class SembakoAdapter(
    private val onDelete: (Sembako) -> Unit
) : ListAdapter<SembakoItem, RecyclerView.ViewHolder>(SembakoItemDiffCallback()) {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerText: TextView = view.findViewById(R.id.tvHeader)
        fun bind(header: SembakoItem.Header) {
            headerText.text = header.title
        }
    }

    inner class SembakoViewHolder(private val binding: ItemSembakoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sembako: Sembako) {
            binding.tvNama.text = sembako.nama
            binding.tvJumlah.text = "Jumlah: ${sembako.jumlah}"
            binding.tvHarga.text = "Harga: Rp ${sembako.harga}"

            binding.root.setOnLongClickListener {
                onDelete(sembako)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SembakoItem.Header -> TYPE_HEADER
            is SembakoItem.Content -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val binding = ItemSembakoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SembakoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SembakoItem.Header -> (holder as HeaderViewHolder).bind(item)
            is SembakoItem.Content -> (holder as SembakoViewHolder).bind(item.sembako)
        }
    }
}

class SembakoItemDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<SembakoItem>() {
    override fun areItemsTheSame(oldItem: SembakoItem, newItem: SembakoItem): Boolean {
        return when {
            oldItem is SembakoItem.Header && newItem is SembakoItem.Header ->
                oldItem.title == newItem.title
            oldItem is SembakoItem.Content && newItem is SembakoItem.Content ->
                oldItem.sembako.id == newItem.sembako.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: SembakoItem, newItem: SembakoItem): Boolean {
        return oldItem == newItem
    }
}