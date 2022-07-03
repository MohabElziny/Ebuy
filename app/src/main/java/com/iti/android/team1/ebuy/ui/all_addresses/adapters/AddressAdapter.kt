package com.iti.android.team1.ebuy.ui.all_addresses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.AddressLayoutBinding
import com.iti.android.team1.ebuy.data.pojo.Address
import com.like.LikeButton
import com.like.OnLikeListener

class AddressAdapter(
    private val onItemClicked: (Address) -> (Unit),
    private inline val onAddSelected: (Address) -> (Unit),
    private val addAsDefAddress: (Long, Int) -> (Unit),
    private val isItCart: Boolean,
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses: ArrayList<Address> = arrayListOf()
    private var defAddressIndex: Int = 0

    private var deletedAddress: Address? = null
    private var deletedAddressAtIndex: Int? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setAddresses(newList: List<Address>) {
        addresses = ArrayList(newList)
        notifyDataSetChanged()
    }

    fun deleteItemAtIndex(index: Int): Long {
        deletedAddress = addresses[index]
        val deletedAddressId: Long? = deletedAddress?.id
        deletedAddressAtIndex = index
        addresses.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, addresses.size)
        if (index == addresses.size)
            defAddressIndex -= 1
        return deletedAddressId ?: 0L
    }

    fun restoreDeletedAddress() {
        val index = deletedAddressAtIndex ?: addresses.size
        val address = deletedAddress ?: Address()
        addresses.add(index, address)
        notifyItemInserted(index)
        notifyItemRangeChanged(index, addresses.size)
    }

    fun changeDefaultAddress(index: Int) {
        addresses[defAddressIndex].default = false
        addresses[index].default = true
        notifyItemChanged(defAddressIndex)
        notifyItemRangeChanged(index, addresses.size)
        defAddressIndex = index
    }

    inner class AddressViewHolder(private val binding: AddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val address: Address
            get() = addresses[bindingAdapterPosition]

        init {
            if (isItCart)
                binding.parent.setOnClickListener {
                    onAddSelected(address)
                }
            else
                binding.parent.setOnClickListener { onItemClicked(address) }

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
            binding.city.text = address.city
            binding.country.text = address.country
            binding.province.text = address.province.plus(", ")
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