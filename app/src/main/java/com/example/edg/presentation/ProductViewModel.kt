package com.example.edg.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edg.common.Loading
import com.example.edg.common.Result
import com.example.edg.data.domain.model.Product
import com.example.edg.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<Result<List<Product>>>()
    val products: LiveData<Result<List<Product>>> = _products

    private val _favouriteProducts = MutableLiveData<List<Product>>()
    val favouriteProducts: LiveData<List<Product>> = _favouriteProducts

    fun getProducts() {
        _products.postValue(Loading)
        viewModelScope.launch {
            val result = productRepository.getProducts()
            _products.postValue(result)
        }
    }

    fun getFavouriteProducts() {
        viewModelScope.launch {
            val result = productRepository.getFavouriteProducts()
            _favouriteProducts.postValue(result)
        }
    }

    fun toggleFavouriteForProduct(productId: String): Product {
        return productRepository.toggleFavouriteForProduct(productId)
    }

    fun removeFavourite(productId: String) {
        productRepository.toggleFavouriteForProduct(productId)
        val result = productRepository.getFavouriteProducts()
        _favouriteProducts.postValue(result)
    }
}
