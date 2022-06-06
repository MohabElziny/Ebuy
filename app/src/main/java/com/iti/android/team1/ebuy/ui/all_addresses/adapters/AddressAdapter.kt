package com.iti.android.team1.ebuy.ui.all_addresses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.AddressLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Addresses

class AddressAdapter(
    private val onItemClick: (Int) -> (Unit),
    private val onDeleteClick: (Addresses) -> (Unit),
    private val onEditClick: (Addresses) -> (Unit),
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses: List<Addresses> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setAddresses(newList: List<Addresses>) {
        addresses = newList
        notifyDataSetChanged()
    }

    inner class AddressViewHolder(binding: AddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val address: Addresses
            get() = addresses[bindingAdapterPosition]

        init {
            binding.parent.setOnClickListener { onItemClick(bindingAdapterPosition) }
            binding.imageDelete.setOnClickListener { onDeleteClick(address) }
            binding.imageDelete.setOnClickListener { onEditClick(address) }
        }

        fun bindView() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressViewHolder(AddressLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) = holder.bindView()

    override fun getItemCount(): Int = addresses.size
}