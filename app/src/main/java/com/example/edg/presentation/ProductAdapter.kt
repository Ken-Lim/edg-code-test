package com.example.edg.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edg.R
import com.example.edg.common.toCurrencyString
import com.example.edg.data.domain.model.Product
import com.google.android.material.button.MaterialButton

class ProductAdapter(productsList: List<Product>, private val listener: Callback) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var _productsList: MutableList<Product> = mutableListOf()

    init {
        _productsList = productsList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item_product, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return _productsList.size
    }

    fun updateProduct(product: Product, position: Int) {
        _productsList[position] = product
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val root: ConstraintLayout = itemView.findViewById(R.id.root)
        private val tvBrand: TextView = itemView.findViewById(R.id.tvBrand)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvPriceValue: TextView = itemView.findViewById(R.id.tvPriceValue)
        private val tvPriceMessage: TextView = itemView.findViewById(R.id.tvPriceMessage)
        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        private val btnFavourite: MaterialButton = itemView.findViewById(R.id.btnFavourite)

        fun bind(position: Int) {
            val product = _productsList[position]
            root.setOnClickListener {
                listener.onItemClicked(product)
            }
            tvBrand.text = product.brand
            tvTitle.text = product.title
            tvPriceValue.text = product.price[0].value.toCurrencyString()
            tvPriceMessage.text = product.price[0].message
            btnFavourite.isSelected = product.isFavourite
            btnFavourite.setOnClickListener {
                listener.onFavouriteToggle(product, position)
            }
            Glide.with(itemView.context)
                .load(product.imageURL)
                .placeholder(R.drawable.ic_wine_placeholder_20dp)
                .into(ivImage)
        }
    }

    interface Callback {
        fun onItemClicked(product: Product)
        fun onFavouriteToggle(product: Product, position: Int)
    }
}
