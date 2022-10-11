package com.example.eda.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Products Fragment"
    }
    val text: LiveData<String> = _text
}
