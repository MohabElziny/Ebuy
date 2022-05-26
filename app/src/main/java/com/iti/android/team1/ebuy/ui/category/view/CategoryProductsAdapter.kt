package com.iti.android.team1.ebuy.ui.category.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.ProductLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Product

class CategoryProductsAdapter() :
    RecyclerView.Adapter<CategoryProductsAdapter.ProductHolder>() {

    private var products: List<Product> = emptyList()

    inner class ProductHolder(val binding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(product: Product) {
            binding.txtProductName.text = product.productName
            binding.txtProductPrice.text =
                product.productVariants?.get(0)?.productVariantPrice.toString()
            Glide.with(binding.root.context).load(products[position].productImage?.imageURL)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding: ProductLayoutBinding =
            ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindData(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun setList(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }


}