package com.example.pencatatansembako

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val sembakoViewModel: SembakoViewModel by viewModels()
    private lateinit var sembakoRecyclerView: RecyclerView
    private lateinit var sembakoAdapter: SembakoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sembakoRecyclerView = findViewById(R.id.rvSembako)
        sembakoRecyclerView.layoutManager = LinearLayoutManager(this)

        sembakoViewModel.sembakoDatabase = SembakoDatabase.getInstance(this)

        sembakoAdapter = SembakoAdapter { sembako ->
            showDeleteDialog(sembako)
        }

        sembakoRecyclerView.adapter = sembakoAdapter

        sembakoViewModel.sembakoList.observe(this) { list ->
            sembakoAdapter.submitList(list)
        }

        findViewById<android.widget.Button>(R.id.btnAddSembako).setOnClickListener {
            showAddDialog()
        }

        sembakoViewModel.getSembako()
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_sembako, null)
        val namaInput = dialogView.findViewById<EditText>(R.id.etNama)
        val jumlahInput = dialogView.findViewById<EditText>(R.id.etJumlah)
        val hargaInput = dialogView.findViewById<EditText>(R.id.etHarga)

        AlertDialog.Builder(this)
            .setTitle("Tambah Sembako")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = namaInput.text.toString().trim()
                val jumlah = jumlahInput.text.toString().toDoubleOrNull()
                val harga = hargaInput.text.toString().toDoubleOrNull()

                if (nama.isNotEmpty() && jumlah != null && harga != null) {
                    sembakoViewModel.insertSembako(Sembako(nama = nama, jumlah = jumlah, harga = harga))
                    Toast.makeText(this, "Data ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Isi data dengan benar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteDialog(sembako: Sembako) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Sembako")
            .setMessage("Yakin ingin menghapus \"${sembako.nama}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                sembakoViewModel.deleteSembako(sembako)
                Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
