package com.example.edg.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.edg.common.Error
import com.example.edg.common.Success
import com.example.edg.data.domain.model.Product
import com.example.edg.data.network.ProductService
import com.example.edg.utils.MockTestUtil.mockProductsList
import com.example.edg.utils.MockTestUtil.mockProductsResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    @RelaxedMockK
    private lateinit var productService: ProductService

    private lateinit var productRepository: ProductRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = ProductRepositoryImpl(productService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun shouldReturnSuccessResultWhenProductServiceReturnValidData() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // WHEN getProducts() is called
        val result = productRepository.getProducts()

        // THEN result should be Success
        assertThat(result).isEqualTo(
            Success(mockProductsList())
        )
    }

    @Test
    fun shouldReturnErrorResultWhenProductServiceReturnException() = runTest {
        // GIVEN productService return exception
        val dummyException = Exception("This is a dummy exception")
        coEvery { productService.getProducts() } throws dummyException

        // WHEN getProducts() is called
        val result = productRepository.getProducts()

        // THEN result should be Error
        assertThat((result as Error).exception.message).isEqualTo(
            Error(dummyException).exception.message
        )
    }

    @Test
    fun shouldReturnEmptyFavouriteProductsListWhenInitialised() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() return Success result
        productRepository.getProducts()

        // WHEN getFavouriteProducts() is called
        val favouriteProductsList = productRepository.getFavouriteProducts()

        // THEN result should be Success
        val emptyList: List<Product> = listOf()
        assertThat(favouriteProductsList).isEqualTo(emptyList)
    }

    @Test
    fun shouldSetFavouriteToTrueWhenToggleIsCalledForNonFavouriteProduct() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() return Success result
        val result = productRepository.getProducts()
        val productsList = (result as Success).data

        // GIVEN product isFavourite is false
        val product = productsList[0]
        assertThat(product.isFavourite).isEqualTo(false)

        // WHEN toggleFavouriteForProduct() is called
        productRepository.toggleFavouriteForProduct(product.id)

        // THEN product isFavourite is set to true
        assertThat(product.isFavourite).isEqualTo(true)
    }

    @Test
    fun shouldReturnProductInFavouriteProductsListWhenProductIsSetAsFavourite() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() return Success result
        val result = productRepository.getProducts()
        val productsList = (result as Success).data

        // GIVEN favouriteProductsList is empty
        var favouriteProductsList = productRepository.getFavouriteProducts()
        val emptyList: List<Product> = listOf()
        assertThat(favouriteProductsList).isEqualTo(emptyList)

        // GIVEN product isFavourite is false
        val product = productsList[0]
        assertThat(product.isFavourite).isEqualTo(false)

        // GIVEN product is set as favourite
        productRepository.toggleFavouriteForProduct(product.id)
        assertThat(product.isFavourite).isEqualTo(true)

        // THEN favouriteProductsList should not be empty can contains product
        favouriteProductsList = productRepository.getFavouriteProducts()
        assertThat(favouriteProductsList.size).isEqualTo(1)
        assertThat(favouriteProductsList[0]).isEqualTo(product)
        assertThat(favouriteProductsList[0].isFavourite).isEqualTo(true)
    }

    @Test
    fun shouldSetProductAsNonFavouriteIfItIsCurrentlyAFavouriteProductAndRemoveItFromFavouriteProductsList() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() return Success result
        val result = productRepository.getProducts()
        val productsList = (result as Success).data

        // GIVEN favouriteProductsList is empty
        var favouriteProductsList = productRepository.getFavouriteProducts()
        val emptyList: List<Product> = listOf()
        assertThat(favouriteProductsList).isEqualTo(emptyList)

        // GIVEN product isFavourite is false
        val product = productsList[0]
        assertThat(product.isFavourite).isEqualTo(false)

        // GIVEN product is set as favourite
        productRepository.toggleFavouriteForProduct(product.id)
        assertThat(product.isFavourite).isEqualTo(true)

        // GIVEN favouriteProductsList contains product
        favouriteProductsList = productRepository.getFavouriteProducts()
        assertThat(favouriteProductsList.size).isEqualTo(1)
        assertThat(favouriteProductsList[0]).isEqualTo(product)
        assertThat(favouriteProductsList[0].isFavourite).isEqualTo(true)

        // GIVEN product is toggleFavouriteForProduct() is called for the product
        productRepository.toggleFavouriteForProduct(product.id)

        // THEN product is not a favourite and remove from favouriteProductsList
        assertThat(product.isFavourite).isEqualTo(false)
        favouriteProductsList = productRepository.getFavouriteProducts()
        assertThat(favouriteProductsList).isEqualTo(emptyList)
    }
}
