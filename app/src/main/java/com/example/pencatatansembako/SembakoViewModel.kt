package com.example.pencatatansembako

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SembakoViewModel : ViewModel() {

    var sembakoDatabase: SembakoDatabase? = null
    private val _sembakoList = MutableLiveData<List<SembakoItem>>()
    val sembakoList: LiveData<List<SembakoItem>> = _sembakoList

    fun getSembako() {
        viewModelScope.launch(Dispatchers.IO) {
            val rawList = sembakoDatabase?.sembakoDao()?.getAll() ?: emptyList()
            val groupedList = groupSembakoItems(rawList)
            _sembakoList.postValue(groupedList)
        }
    }

    private fun groupSembakoItems(list: List<Sembako>): List<SembakoItem> {
        val grouped = list.groupBy {
            if (it.nama.lowercase().contains("beras") || it.nama.lowercase().contains("gula"))
                "Pokok"
            else
                "Tambahan"
        }

        val items = mutableListOf<SembakoItem>()
        grouped.forEach { (kategori, sembakoList) ->
            items.add(SembakoItem.Header(kategori))
            items.addAll(sembakoList.map { SembakoItem.Content(it) })
        }
        return items
    }

    fun insertSembako(sembako: Sembako) {
        viewModelScope.launch(Dispatchers.IO) {
            sembakoDatabase?.sembakoDao()?.insert(sembako)
            getSembako() // This will automatically regroup the items
        }
    }

    fun deleteSembako(sembako: Sembako) {
        viewModelScope.launch(Dispatchers.IO) {
            sembakoDatabase?.sembakoDao()?.delete(sembako)
            getSembako() // This will automatically regroup the items
        }
    }
}