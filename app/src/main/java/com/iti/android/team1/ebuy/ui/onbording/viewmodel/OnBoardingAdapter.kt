package com.iti.android.team1.ebuy.ui.onbording.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.BoardingLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Boarding


class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    private lateinit var context: Context

    val list = listOf(
        Boarding(title = "Search",
            dec = "Search and find your desired product from big varieties of worldwide brands.",
            image = R.drawable.ic_shope_),
        Boarding(title = "Pay",
            dec = "Pay online via your debit or credit card. We work with the best Egyptian banks to protect your payments.",
            image = R.drawable.ic_credit_card),
        Boarding(title = "Receive",
            dec = "Receive for your package to reach your home door within less than 3 hours",
            image = R.drawable.ic_wait),
    )

    inner class OnBoardingViewHolder(private val binding: BoardingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val currentItem: Boarding
            get() = list[bindingAdapterPosition]

        fun bind() {
            binding.desc.text = currentItem.dec
            binding.title.text = currentItem.title
            Glide.with(context).load(currentItem.image).into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        context = parent.context
        return OnBoardingViewHolder(
            BoardingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) = holder.bind()

    override fun getItemCount() = list.size
}