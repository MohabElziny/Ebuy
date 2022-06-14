package com.iti.android.team1.ebuy.ui.all_addresses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.AddressLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Address
import com.like.LikeButton
import com.like.OnLikeListener

class AddressAdapter(
    private val onItemClick: (Int) -> (Unit),
    private val onDeleteClick: (Address, Int) -> (Unit),
    private val onEditClick: (Address) -> (Unit),
    private val addAsDefAddress: (Long, Int) -> (Unit),
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses: ArrayList<Address> = arrayListOf()
    private var defAddressIndex: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setAddresses(newList: List<Address>) {
        addresses = ArrayList(newList)
        notifyDataSetChanged()
    }

    fun deleteItemAtIndex(index: Int) {
        addresses.removeAt(index)
        notifyItemRemoved(index)
        if (index == addresses.size)
            defAddressIndex -= 1
    }

    fun changeDefaultAddress(index: Int) {
        addresses[defAddressIndex].default = false
        addresses[index].default = true
        notifyItemChanged(defAddressIndex)
        defAddressIndex = index
    }

    inner class AddressViewHolder(private val binding: AddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val address: Address
            get() = addresses[bindingAdapterPosition]

        init {
            binding.parent.setOnClickListener { onItemClick(bindingAdapterPosition) }
            binding.imageDelete.setOnClickListener {
                onDeleteClick(address, bindingAdapterPosition)
            }
            binding.imageEdit.setOnClickListener { onEditClick(address) }

            binding.defBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    addAsDefAddress(address.id ?: 0, bindingAdapterPosition)
                }

                override fun unLiked(likeButton: LikeButton?) {}

            })
        }

        fun bindView() {
            binding.name.text = address.address1
            binding.phone.text = "Tel: ${address.phone}"
            binding.address.text = address.address1
            binding.city.text = address.city
            binding.country.text = address.country
            binding.province.text = address.province
            if (addresses.size > 1)
                binding.defBtn.visibility = View.VISIBLE

            if (address.default == true) {
                binding.defBtn.isLiked = true
                defAddressIndex = bindingAdapterPosition
            } else {
                binding.defBtn.isLiked = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressViewHolder(AddressLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) = holder.bindView()

    override fun getItemCount(): Int = addresses.size
}