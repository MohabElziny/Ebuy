package com.iti.android.team1.ebuy.ui.category.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.CategoryRowBinding
import com.iti.android.team1.ebuy.model.pojo.Category

class CategoriesAdapter ( var categories: List<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {

    class CategoriesHolder(binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val btn=binding.categoryRowBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        return CategoriesHolder(CategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.btn.text = categories[position].categoryTitle
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setList(categoryList: List<Category>)
    {
        categories=categoryList
        notifyDataSetChanged()
    }


}