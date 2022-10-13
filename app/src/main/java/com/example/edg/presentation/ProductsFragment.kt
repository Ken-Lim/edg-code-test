package com.example.edg.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.edg.R
import com.example.edg.common.Error
import com.example.edg.common.Loading
import com.example.edg.common.Result
import com.example.edg.common.Success
import com.example.edg.data.domain.model.Product
import com.example.edg.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductAdapter.Callback {

    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productViewModel.products.observe(viewLifecycleOwner, ::handleResult)
        binding.recyclerView.addItemDecoration(
            CustomItemDecorator(resources.getDimension(R.dimen.extra_small_padding).toInt())
        )

        return root
    }

    override fun onResume() {
        super.onResume()
        productViewModel.getProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleResult(result: Result<List<Product>>) {
        when (result) {
            is Loading -> {
                binding.progressBar.isVisible = true
                binding.recyclerView.isVisible = false
                binding.tvMessage.isVisible = false
            }
            is Success -> {
                binding.progressBar.isVisible = false
                binding.tvMessage.isVisible = false
                binding.recyclerView.isVisible = true
                adapter = ProductAdapter(result.data, this)
                binding.recyclerView.adapter = adapter
            }
            is Error -> {
                binding.recyclerView.isVisible = false
                binding.progressBar.isVisible = false
                binding.tvMessage.isVisible = true
                binding.tvMessage.text = result.exception.message
            }
        }
    }

    override fun onItemClicked(product: Product) {
        val action = ProductsFragmentDirections.nextAction(product)
        findNavController().navigate(action)
    }

    override fun onFavouriteToggle(product: Product, position: Int) {
        val updatedProduct = productViewModel.toggleFavouriteForProduct(product.id)
        adapter.updateProduct(updatedProduct, position)
    }
}
