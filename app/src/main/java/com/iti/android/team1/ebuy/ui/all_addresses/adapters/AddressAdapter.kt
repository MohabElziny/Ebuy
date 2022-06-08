package com.iti.android.team1.ebuy.ui.all_addresses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.AddressLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Address

class AddressAdapter(
    private val onItemClick: (Int) -> (Unit),
    private val onDeleteClick: (Address, Int) -> (Unit),
    private val onEditClick: (Address) -> (Unit),
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses: ArrayList<Address> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setAddresses(newList: List<Address>) {
        addresses.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteItemAtIndex(index: Int) {
        addresses.removeAt(index)
        notifyItemRemoved(index)
    }

    inner class AddressViewHolder(private val binding: AddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val address: Address
            get() = addresses[bindingAdapterPosition]

        init {
            binding.parent.setOnClickListener { onItemClick(bindingAdapterPosition) }
            binding.imageDelete.setOnClickListener {
                onDeleteClick(address,
                    bindingAdapterPosition)
            }
            binding.imageEdit.setOnClickListener { onEditClick(address) }
        }

        fun bindView() {
            binding.name.text = address.address1
            binding.phone.text = "Tel: ${address.phone}"
            binding.address.text = address.address1
            binding.city.text = address.city
            binding.country.text = address.country
            binding.province.text = address.province
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressViewHolder(AddressLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) = holder.bindView()

    override fun getItemCount(): Int = addresses.size
}