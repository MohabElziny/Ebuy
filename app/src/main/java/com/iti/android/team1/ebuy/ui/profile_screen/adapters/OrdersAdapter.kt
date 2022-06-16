package com.iti.android.team1.ebuy.ui.profile_screen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.OrdersCardRowBinding
import com.iti.android.team1.ebuy.model.pojo.Order

class OrdersAdapter(private val onClickOrderItem: (orderName: String, orderFinancialStatus: String, orderStatus: String) -> Unit) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {
    private var _orderList: List<Order> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setOrderList(orderList: List<Order>) {
        _orderList = orderList
        notifyDataSetChanged()
    }

    inner class OrdersViewHolder(val binding: OrdersCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val order get() = _orderList[bindingAdapterPosition]

        init {
            binding.ordersCard.setOnClickListener {
                onClickOrderItem(order.name ?: "",
                    order.financialStatus ?: "",
                    order.orderStatus ?: "")
            }
        }

        fun bindOrderCard() {
            binding.txtOrderPrice.text = order.totalPrice ?: ""
            binding.txtOrderDate.text = order.createdAt ?: ""
            binding.txtOrderNumber.text = "${order.orderNumber ?: 0}"
            binding.txtOrderStatus.text = order.financialStatus ?: ""


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
    }

    override fun getItemCount(): Int {
        return _orderList.size
    }

}