package com.example.pencatatansembako

import androidx.recyclerview.widget.DiffUtil

class SembakoDiffCallback : DiffUtil.ItemCallback<Sembako>() {
    override fun areItemsTheSame(oldItem: Sembako, newItem: Sembako): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Sembako, newItem: Sembako): Boolean {
        return oldItem == newItem
    }
}
