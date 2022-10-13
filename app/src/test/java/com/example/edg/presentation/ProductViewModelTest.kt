package com.example.edg.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.edg.common.Loading
import com.example.edg.common.Result
import com.example.edg.common.Success
import com.example.edg.data.domain.model.Product
import com.example.edg.data.network.ProductService
import com.example.edg.data.repository.ProductRepository
import com.example.edg.data.repository.ProductRepositoryImpl
import com.example.edg.utils.MockTestUtil.mockProductsList
import com.example.edg.utils.MockTestUtil.mockProductsResponse
import com.example.edg.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verifySequence
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
class ProductViewModelTest {
    @RelaxedMockK
    private lateinit var productService: ProductService

    private lateinit var productRepository: ProductRepository

    private lateinit var viewModel: ProductViewModel

    @RelaxedMockK
    private lateinit var productsObserver: Observer<Result<List<Product>>>

    @RelaxedMockK
    private lateinit var favouriteProductsObserver: Observer<List<Product>>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = ProductRepositoryImpl(productService)
        viewModel = ProductViewModel(productRepository)
        viewModel.products.observeForever(productsObserver)
        viewModel.favouriteProducts.observeForever(favouriteProductsObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun shouldObserveLoadingThenSuccessWhenProductRepositoryReturnValidData() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // WHEN getProducts() is called
        viewModel.getProducts()

        // THEN products LiveData is observed in sequence of Loading then Success
        verifySequence {
            productsObserver.onChanged(Loading)
            productsObserver.onChanged(Success(mockProductsList()))
        }
        assertThat(viewModel.products.getOrAwaitValue()).isEqualTo(Success(mockProductsList()))
    }

    @Test
    fun shouldReturningEmptyFavouriteProductsListInitially() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() is called
        viewModel.getProducts()

        // WHEN getFavouriteProducts() is called
        viewModel.getFavouriteProducts()

        // THEN favouriteProductsList is empty
        assertThat(viewModel.favouriteProducts.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun shouldToggleIsFavouriteToTrueWhenExistingIsFavouriteIsFalse() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() is called
        viewModel.getProducts()
        verifySequence {
            productsObserver.onChanged(Loading)
            productsObserver.onChanged(Success(mockProductsList()))
        }
        assertThat(viewModel.products.getOrAwaitValue()).isEqualTo(Success(mockProductsList()))

        // GIVEN favouriteProductList is empty
        viewModel.getFavouriteProducts()
        assertThat(viewModel.favouriteProducts.getOrAwaitValue()).isEmpty()

        // GIVEN product isFavourite is false
        val result = viewModel.products.getOrAwaitValue()
        val product = (result as Success).data[0]
        assertThat(product.isFavourite).isFalse()

        // WHEN toggleFavouriteForProduct() is called and getFavouriteProducts() is called
        val updatedProduct = viewModel.toggleFavouriteForProduct(product.id)
        viewModel.getFavouriteProducts()

        // THEN product isFavourite is true and favouriteProductList not empty
        assertThat(updatedProduct.isFavourite).isEqualTo(true)
        val favouriteProductList = viewModel.favouriteProducts.getOrAwaitValue()
        assertThat(favouriteProductList).isNotEmpty()
        assertThat(favouriteProductList.contains(updatedProduct)).isTrue()
    }

    @Test
    fun shouldRemoveProductFromFavouriteProductsListWhenRemoveToggleIsCalled() = runTest {
        // GIVEN productService return valid response
        coEvery { productService.getProducts() } answers { mockProductsResponse() }

        // GIVEN getProducts() is called
        viewModel.getProducts()
        verifySequence {
            productsObserver.onChanged(Loading)
            productsObserver.onChanged(Success(mockProductsList()))
        }
        assertThat(viewModel.products.getOrAwaitValue()).isEqualTo(Success(mockProductsList()))

        // GIVEN favouriteProductList is empty
        viewModel.getFavouriteProducts()
        assertThat(viewModel.favouriteProducts.getOrAwaitValue()).isEmpty()

        // GIVEN product isFavourite is false
        val result = viewModel.products.getOrAwaitValue()
        val product = (result as Success).data[0]
        assertThat(product.isFavourite).isFalse()

        // GIVEN toggleFavouriteForProduct() is called and getFavouriteProducts() is called
        val updatedProduct = viewModel.toggleFavouriteForProduct(product.id)
        viewModel.getFavouriteProducts()

        // GIVEN product isFavourite is true and favouriteProductList not empty
        assertThat(updatedProduct.isFavourite).isEqualTo(true)
        var favouriteProductList = viewModel.favouriteProducts.getOrAwaitValue()
        assertThat(favouriteProductList).isNotEmpty()
        assertThat(favouriteProductList.contains(updatedProduct)).isTrue()

        // WHEN removeFavourite() is called
        viewModel.removeFavourite(updatedProduct.id)

        // THEN product is removed from favouriteProductList
        favouriteProductList = viewModel.favouriteProducts.getOrAwaitValue()
        assertThat(favouriteProductList).isEmpty()
    }
}
