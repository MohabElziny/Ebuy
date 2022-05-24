package com.iti.android.team1.ebuy.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.BrandsLayoutBinding

class HomeRecyclerAdapter : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(binding: BrandsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(
            BrandsLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}