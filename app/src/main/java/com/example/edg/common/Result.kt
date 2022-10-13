package com.example.edg.common

sealed class Result<out R> {
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

data class Success<out T>(val data: T) : Result<T>()

data class Error(val exception: Exception) : Result<Nothing>()

object Loading : Result<Nothing>()
