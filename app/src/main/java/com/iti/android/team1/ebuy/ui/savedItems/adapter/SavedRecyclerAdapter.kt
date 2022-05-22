package com.iti.android.team1.ebuy.ui.savedItems.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding

class SavedRecyclerAdapter : RecyclerView.Adapter<SavedRecyclerAdapter.SavedItemsViewHolder>() {

    inner class SavedItemsViewHolder(binding: SavedItemsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemsViewHolder =
        SavedItemsViewHolder(
            SavedItemsLayoutBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    override fun onBindViewHolder(holder: SavedItemsViewHolder, position: Int) = bindView()

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    private fun bindView() {}
}