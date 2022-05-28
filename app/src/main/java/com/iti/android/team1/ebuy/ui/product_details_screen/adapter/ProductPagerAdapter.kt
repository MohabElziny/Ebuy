package com.iti.android.team1.ebuy.ui.product_details_screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.AdLayoutBinding
import com.iti.android.team1.ebuy.databinding.ProductLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.ProductImage

class ProductPagerAdapter() : RecyclerView.Adapter<ProductPagerAdapter.ProductViewHolder>() {
    private var productImages: List<ProductImage> = emptyList<ProductImage>()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductImagesAdapter(productImages: List<ProductImage>) {
        this.productImages = productImages
        notifyDataSetChanged()
    }

    class ProductViewHolder(var binding: AdLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindProductImageAdapter(productImage: ProductImage) {
            Glide.with(binding.root.context).load(productImage.imageURL).fitCenter()
                .into(binding.adImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            AdLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.adImage.scaleType = ImageView.ScaleType.FIT_CENTER
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bindProductImageAdapter(productImages[position])


    override fun getItemCount() = productImages.size
}