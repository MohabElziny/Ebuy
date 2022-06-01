package com.iti.android.team1.ebuy.ui.savedItems.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

class SavedRecyclerAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onIncreaseClick: (FavoriteProduct, Int) -> Unit,
    private val onDecreaseClick: (FavoriteProduct, Int) -> Unit,
) : RecyclerView.Adapter<SavedRecyclerAdapter.SavedItemsViewHolder>() {

    private var favorites: List<FavoriteProduct> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapterList(newList: List<FavoriteProduct>) {
        favorites = newList
        notifyDataSetChanged()
    }

    inner class SavedItemsViewHolder(private val binding: SavedItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parent.setOnClickListener {
                onItemClick(favorites[bindingAdapterPosition].productID)
            }

            binding.btnDecrease.setOnClickListener {
                onDecreaseClick(favorites[bindingAdapterPosition],
                    bindingAdapterPosition)
            }

            binding.btnIncrease.setOnClickListener {
                onIncreaseClick(favorites[bindingAdapterPosition],
                    bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val currentProduct = favorites[bindingAdapterPosition]
            val calculatedPrice = currentProduct.productPrice * currentProduct.noOfSavedItems
            Glide.with(binding.root.context).load(currentProduct.productImageUrl)
                .into(binding.savedImage)
            binding.savedProductName.text = currentProduct.productName
            binding.savedPrice.text = "$calculatedPrice EGP"
            binding.noOfItems.text = currentProduct.noOfSavedItems.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemsViewHolder =
        SavedItemsViewHolder(SavedItemsLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: SavedItemsViewHolder, position: Int) = holder.bindView()

    override fun getItemCount(): Int = favorites.size

}