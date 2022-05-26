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

    class CategoriesHolder(val binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val btn=binding.categoryRowBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        return CategoriesHolder(CategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoriesHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.btn.text = categories[position].categoryTitle
        holder.btn.setOnClickListener {
            btnIndex=position
            if(position==0)
                onCategoryBtnClick(0,holder.btn.text as String)
            else
            onCategoryBtnClick(categories[position].categoryId,holder.btn.text as String)
            notifyDataSetChanged()
        }
        if (btnIndex==position)
            holder.btn.setBackgroundColor(ContextCompat.getColor(holder.binding.root.context,R.color.Primary))
        else
            holder.btn.setBackgroundColor(ContextCompat.getColor(holder.binding.root.context,R.color.white))
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