package com.example.pencatatansembako

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pencatatansembako.databinding.ActivityMainBinding
import com.example.pencatatansembako.databinding.DialogAddSembakoBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sembakoViewModel: SembakoViewModel by viewModels()
    private lateinit var sembakoAdapter: SembakoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sembakoViewModel.sembakoDatabase = SembakoDatabase.getInstance(this)

        sembakoAdapter = SembakoAdapter { sembako ->
            showDeleteDialog(sembako)
        }

        // Gunakan GridLayoutManager + SpanSizeLookup
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (sembakoAdapter.getItemViewType(position)) {
                    0 -> 2 // Header full span
                    else -> 1
                }
            }
        }

        binding.rvSembako.layoutManager = layoutManager
        binding.rvSembako.adapter = sembakoAdapter

        binding.btnAddSembako.setOnClickListener {
            showAddDialog()
        }

        sembakoViewModel.sembakoList.observe(this) { list ->
            sembakoAdapter.submitList(list)
        }

        sembakoViewModel.getSembako()

        // ItemTouchHelper for swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // kita tidak mendukung drag & drop sekarang
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = sembakoAdapter.currentList[position]

                if (item is SembakoItem.Content) {
                    sembakoViewModel.deleteSembako(item.sembako)
                    Toast.makeText(this@MainActivity, "Item dihapus dengan swipe", Toast.LENGTH_SHORT).show()
                } else {
                    sembakoAdapter.notifyItemChanged(position) // reset swipe jika yang diswipe adalah header
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSembako)
    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddSembakoBinding.inflate(LayoutInflater.from(this))

        AlertDialog.Builder(this)
            .setTitle("Tambah Sembako")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = dialogBinding.etNama.text.toString().trim()
                val jumlah = dialogBinding.etJumlah.text.toString().toDoubleOrNull()
                val harga = dialogBinding.etHarga.text.toString().toDoubleOrNull()

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