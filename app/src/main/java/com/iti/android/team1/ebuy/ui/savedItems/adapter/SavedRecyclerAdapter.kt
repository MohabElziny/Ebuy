package com.iti.android.team1.ebuy.ui.savedItems.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Product
import com.like.LikeButton
import com.like.OnLikeListener

class SavedRecyclerAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onUnLike: (Long, Int) -> Unit,
) : RecyclerView.Adapter<SavedRecyclerAdapter.SavedItemsViewHolder>() {

    private var favorites: MutableList<Product> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapterList(newList: List<Product>) {
        favorites = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItemFromList(index: Int) {
        if (index < 0) return
        favorites.removeAt(index)
        notifyItemRemoved(index)
    }

    inner class SavedItemsViewHolder(private val binding: SavedItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val currentProduct: Product
            get() = favorites[bindingAdapterPosition]

        init {
            binding.parent.setOnClickListener {
                onItemClick(currentProduct.productID ?: 0)
            }

            binding.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {}

                override fun unLiked(likeButton: LikeButton?) {
                    onUnLike(currentProduct.productID ?: 0, bindingAdapterPosition)
                }
            })

        }

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val context = binding.root.context
            val variant = currentProduct.productVariants?.get(0)
            Glide.with(binding.root.context).load(currentProduct.productImage?.imageURL)
                .into(binding.savedImage)
            binding.savedProductName.text = currentProduct.productName
            binding.savedPrice.text = "${variant?.productVariantPrice} EGP"
            binding.likeBtn.isLiked = true
            if ((variant?.productVariantInventoryQuantity ?: 0) > 0) {
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