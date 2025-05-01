package com.example.pencatatansembako

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sembako(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nama: String,
    val jumlah: Double,
    val harga: Double
)
