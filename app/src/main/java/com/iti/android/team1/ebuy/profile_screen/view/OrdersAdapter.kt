package com.iti.android.team1.ebuy.profile_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.OrdersCardRowBinding

class OrdersAdapter() :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    class OrdersViewHolder(val binding: OrdersCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindOrderCard() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrdersCardRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bindOrderCard()
        holder.binding.ordersCard.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 0
    }

}