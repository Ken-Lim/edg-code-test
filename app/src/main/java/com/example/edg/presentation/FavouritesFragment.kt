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
import com.example.edg.data.domain.model.Product
import com.example.edg.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(), ProductAdapter.Callback {

    private var _binding: FragmentFavouritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productViewModel.favouriteProducts.observe(viewLifecycleOwner, ::updateUi)
        binding.recyclerView.addItemDecoration(
            CustomItemDecorator(resources.getDimension(R.dimen.extra_small_padding).toInt())
        )

        return root
    }

    override fun onResume() {
        super.onResume()
        productViewModel.getFavouriteProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(favouriteProductsList: List<Product>) {
        if (favouriteProductsList.isEmpty()) {
            binding.recyclerView.isVisible = false
            binding.tvMessage.isVisible = true
        } else {
            binding.recyclerView.isVisible = true
            binding.tvMessage.isVisible = false
            adapter = ProductAdapter(favouriteProductsList, this)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onItemClicked(product: Product) {
        val action = FavouritesFragmentDirections.nextAction(product)
        findNavController().navigate(action)
    }

    override fun onFavouriteToggle(product: Product, position: Int) {
        productViewModel.removeFavourite(product.id)
    }
}
