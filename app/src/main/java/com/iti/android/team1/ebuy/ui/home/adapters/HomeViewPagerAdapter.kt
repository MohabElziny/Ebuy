package com.iti.android.team1.ebuy.ui.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.AdLayoutBinding
import com.iti.android.team1.ebuy.data.pojo.DiscountCodes

class HomeViewPagerAdapter(private val showDiscountDialog: (DiscountCodes) -> Unit) :
    RecyclerView.Adapter<HomeViewPagerAdapter.DiscountViewHolder>() {

    var discountCodeList = emptyList<DiscountCodes>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class DiscountViewHolder(val binding: AdLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                showDiscountDialog(discountCodeList[bindingAdapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder =
        DiscountViewHolder(AdLayoutBinding.inflate(LayoutInflater.from(
            parent.context), parent, false)
        )


    override fun getItemCount() = discountCodeList.size


    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
    }

}