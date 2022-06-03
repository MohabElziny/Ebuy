package com.iti.android.team1.ebuy.ui.profile_screen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FavoritesCardRowBinding
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Order
import com.like.LikeButton
import com.like.OnLikeListener

class ProfileFavoritesAdapter(
    private inline val onClickItem: (Long) -> Unit,
    private inline val unLikeItem:(Long)->Unit
) :
    RecyclerView.Adapter<ProfileFavoritesAdapter.ProfileFavoritesViewHolder>() {
    private var _favouriteList: List<FavoriteProduct> = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setFavouriteList(favouriteList: List<FavoriteProduct>) {
        _favouriteList = favouriteList
        notifyDataSetChanged()
    }

    inner class ProfileFavoritesViewHolder(val binding: SavedItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val favProduct get() = _favouriteList[bindingAdapterPosition]

        init {
            binding.parent.setOnClickListener {
                onClickItem(favProduct.productID)
            }

            binding.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {}

                override fun unLiked(likeButton: LikeButton?) {
                    unLikeItem(favProduct.productID)
                }
            })
        }

        fun bindFavoriteCard() {
            val context = binding.root.context
            val calculatedPrice = favProduct.productPrice
            Glide.with(binding.root.context).load(favProduct.productImageUrl)
                .into(binding.savedImage)
            binding.savedProductName.text = favProduct.productName
            binding.savedPrice.text = "$calculatedPrice ${favProduct.currency}"
            binding.likeBtn.isLiked = true
            if (favProduct.stock > 0) {
                binding.savedIsInStock.text = context.getString(R.string.in_stock)
                binding.savedIsInStock.setTextColor(context.resources.getColor(R.color.Success))
            } else {
                binding.savedIsInStock.text = context.getString(R.string.out_of_stock)
                binding.savedIsInStock.setTextColor(context.resources.getColor(R.color.Warning))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFavoritesViewHolder {
        return ProfileFavoritesViewHolder(
            SavedItemsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileFavoritesViewHolder, position: Int) {
        holder.bindFavoriteCard()
    }

    override fun getItemCount(): Int {
        return _favouriteList.size
    }

}