package com.example.pencatatansembako

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SembakoViewModel : ViewModel() {

    var sembakoDatabase: SembakoDatabase? = null
    private val _sembakoList = MutableLiveData<List<Sembako>>()
    val sembakoList: LiveData<List<Sembako>> = _sembakoList

    fun getSembako() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = sembakoDatabase?.sembakoDao()?.getAll() ?: emptyList()
            _sembakoList.postValue(list)
        }
    }

    fun insertSembako(sembako: Sembako) {
        viewModelScope.launch(Dispatchers.IO) {
            sembakoDatabase?.sembakoDao()?.insert(sembako)
            getSembako()
        }
    }

    fun deleteSembako(sembako: Sembako) {
        viewModelScope.launch(Dispatchers.IO) {
            sembakoDatabase?.sembakoDao()?.delete(sembako)
            getSembako()
        }
    }
}
