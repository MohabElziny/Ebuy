package com.iti.android.team1.ebuy.ui.profile_screen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.FavoritesCardRowBinding
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Order

class ProfileFavoritesAdapter() :
    RecyclerView.Adapter<ProfileFavoritesAdapter.ProfileFavoritesViewHolder>() {
    private var _favouriteList: List<FavoriteProduct> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setFavouriteList(favouriteList: List<FavoriteProduct>) {
        _favouriteList = favouriteList
        notifyDataSetChanged()
    }

    inner class ProfileFavoritesViewHolder(val binding: FavoritesCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val favProduct get() = _favouriteList[bindingAdapterPosition]

        init {
            binding.btnRemove.setOnClickListener {
                // will go
            }
        }

        fun bindFavoriteCard() {
            binding.txtProductPrice.text = favProduct.productPrice.toString()
            binding.txtProductName.text = favProduct.productName
            Glide.with(binding.root.context)
                .load(favProduct.productImageUrl)
                .into(binding.favoriteImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFavoritesViewHolder {
        return ProfileFavoritesViewHolder(
            FavoritesCardRowBinding.inflate(
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