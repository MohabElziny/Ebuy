package com.iti.android.team1.ebuy.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.BrandsLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Brand
import com.iti.android.team1.ebuy.model.pojo.Brands

class HomeRecyclerAdapter(
    private val context: Context,
    private val onClickBrand: (Long) -> Unit) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    private var brands: List<Brand> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setBrandsList(brandsPOJO: Brands) {
        this.brands = brandsPOJO.brands
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(val binding: BrandsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(brand: Brand) {
            binding.brandName.text = brand.brandTitle
            Glide.with(context)
                .load(brand.brandImage.imageUrl)
                .into(binding.brandImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(
            BrandsLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindData(brands[position])
        holder.binding.parent.setOnClickListener {
            onClickBrand(brands[position].brandID)
        }
    }

    override fun getItemCount(): Int {
        return brands.size
    }
}