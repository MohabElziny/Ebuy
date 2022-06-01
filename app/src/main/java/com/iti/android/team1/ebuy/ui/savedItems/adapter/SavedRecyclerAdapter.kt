package com.iti.android.team1.ebuy.ui.savedItems.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.like.LikeButton
import com.like.OnLikeListener

class SavedRecyclerAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onUnLike: (Long, Int) -> Unit,
) : RecyclerView.Adapter<SavedRecyclerAdapter.SavedItemsViewHolder>() {

    private var favorites: List<FavoriteProduct> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapterList(newList: List<FavoriteProduct>) {
        favorites = newList
        notifyDataSetChanged()
    }


    inner class SavedItemsViewHolder(private val binding: SavedItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var currentProduct: FavoriteProduct
        init {
            binding.parent.setOnClickListener {
                currentProduct = favorites[bindingAdapterPosition]
                onItemClick(currentProduct.productID)
            }

            binding.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {}

                override fun unLiked(likeButton: LikeButton?) {
                    onUnLike(currentProduct.productID, bindingAdapterPosition)
                }
            })

        }

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val context = binding.root.context
            val calculatedPrice = currentProduct.productPrice
            Glide.with(binding.root.context).load(currentProduct.productImageUrl)
                .into(binding.savedImage)
            binding.savedProductName.text = currentProduct.productName
            binding.savedPrice.text = "$calculatedPrice ${currentProduct.currency}"
            binding.likeBtn.isLiked = true
            if (currentProduct.stock > 0) {
                binding.savedIsInStock.text = context.getString(R.string.in_stock)
                binding.savedIsInStock.setTextColor(context.resources.getColor(R.color.Success))
            } else {
                binding.savedIsInStock.text = context.getString(R.string.out_of_stock)
                binding.savedIsInStock.setTextColor(context.resources.getColor(R.color.Warning))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemsViewHolder =
        SavedItemsViewHolder(SavedItemsLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: SavedItemsViewHolder, position: Int) = holder.bindView()

    override fun getItemCount(): Int = favorites.size

}