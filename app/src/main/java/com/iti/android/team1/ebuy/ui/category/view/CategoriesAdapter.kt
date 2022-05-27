package com.iti.android.team1.ebuy.ui.category.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.CategoryRowBinding
import com.iti.android.team1.ebuy.model.pojo.Category

class CategoriesAdapter ( val onCategoryBtnClick : (id:Long,title:String)->Unit ): RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {

    private var btnIndex:Int=0
    private var categories: List<Category> = emptyList()

   inner class CategoriesHolder(val binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(category: Category){
            binding.categoryRowBtn.text=category.categoryTitle
            if (btnIndex==position)
                binding.categoryRowBtn.setBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.Primary))
            else
                binding.categoryRowBtn.setBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.white))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        return CategoriesHolder(CategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.bindData(categories[position])

        holder.binding.categoryRowBtn.setOnClickListener {
            btnIndex=position
            if(position==0)
                onCategoryBtnClick(0,holder.binding.categoryRowBtn.text as String)
            else
            onCategoryBtnClick(categories[position].categoryId,holder.binding.categoryRowBtn.text as String)
            notifyDataSetChanged()
        }

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