package com.iti.android.team1.ebuy.ui.category.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.CategoryCustomRvItemBinding

class CategoryProductsAdapter : RecyclerView.Adapter<CategoryProductsAdapter.Holder>() {

    class Holder(binding: CategoryCustomRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var image = binding.catCustomImage
        var title = binding.catCustomTvPrice
        var imgFavorite = binding.catCustomImgFavo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryCustomRvItemBinding =
            CategoryCustomRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.title.text = "55.6"
        holder.imgFavorite.setOnClickListener {
            holder.imgFavorite.setImageResource(R.drawable.fill_heart_image)
        }

    }

    override fun getItemCount(): Int {
        return 8
    }


}