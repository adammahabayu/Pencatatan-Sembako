package com.example.pencatatansembako

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SembakoAdapter(
    private val onDelete: (Sembako) -> Unit
) : ListAdapter<Sembako, SembakoAdapter.SembakoViewHolder>(SembakoDiffCallback()) {

    inner class SembakoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNama)
        val jumlah: TextView = view.findViewById(R.id.tvJumlah)
        val harga: TextView = view.findViewById(R.id.tvHarga)

        fun bind(sembako: Sembako) {
            nama.text = sembako.nama
            jumlah.text = "Jumlah: ${sembako.jumlah}"
            harga.text = "Harga: Rp ${sembako.harga}"

            itemView.setOnLongClickListener {
                onDelete(sembako)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SembakoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sembako, parent, false)
        return SembakoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SembakoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
