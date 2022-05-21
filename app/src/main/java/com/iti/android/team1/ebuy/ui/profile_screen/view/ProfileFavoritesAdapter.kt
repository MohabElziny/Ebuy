package com.iti.android.team1.ebuy.ui.profile_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.FavoritesCardRowBinding

class ProfileFavoritesAdapter() :
    RecyclerView.Adapter<ProfileFavoritesAdapter.ProfileFavoritesViewHolder>() {

    class ProfileFavoritesViewHolder(val binding: FavoritesCardRowBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bindFavoriteCard(){

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
        holder.binding.btnRemove.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 0
    }

}