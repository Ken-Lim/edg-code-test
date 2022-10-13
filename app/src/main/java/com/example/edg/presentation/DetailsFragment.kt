package com.example.edg.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.edg.R
import com.example.edg.common.toCurrencyString
import com.example.edg.databinding.FragmentDetailsBinding
import com.example.edg.presentation.DetailsFragmentArgs.Companion.fromBundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val product by lazy {
        fromBundle(requireArguments()).product
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBrand.text = product.brand
        binding.tvTitle.text = product.title
        binding.ratingBar.rating = product.ratingCount.toFloat()
        binding.tvPriceValue.text = product.price[0].value.toCurrencyString()
        binding.tvPriceMessage.text = product.price[0].message
        Glide.with(requireContext())
            .load(product.imageURL)
            .placeholder(R.drawable.ic_wine_placeholder_20dp)
            .into(binding.ivImage)
        binding.btnFavourite.isSelected = product.isFavourite
        binding.btnFavourite.setOnClickListener {
            binding.btnFavourite.isSelected = productViewModel.toggleFavouriteForProduct(product.id).isFavourite
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
