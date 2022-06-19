package com.iti.android.team1.ebuy.ui.cart_screen.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.databinding.RecyclerRowProductCartBinding
import com.iti.android.team1.ebuy.model.pojo.CartItem

class CartProductAdapter(
    private val deleteItem: (Int) -> Unit,
    private val increaseQuantity: (Int) -> Unit,
    private val decreaseQuantity: (Int) -> Unit,
) : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    private var _cartItems: List<CartItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setCartItems(cartItems: List<CartItem>) {
        this._cartItems = cartItems
        notifyDataSetChanged()
    }

    inner class CartProductViewHolder(var binding: RecyclerRowProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val cartItem get() = _cartItems[bindingAdapterPosition]

        init {
            binding.deleteProduct.setOnClickListener {
                deleteItem(bindingAdapterPosition)
            }
            binding.buttonIncrease.setOnClickListener {
                increaseQuantity(bindingAdapterPosition)
            }
            binding.btnMinus.setOnClickListener {
                decreaseQuantity(bindingAdapterPosition)
            }
        }

        fun bindCartProduct() {
            binding.textProductName.text = cartItem.productName
            binding.textProductSalary.text = cartItem.productVariantPrice.toString().plus(" EGP")
            binding.textProductQuantity.text = cartItem.customerProductQuantity.toString()
            Glide.with(binding.root.context).load(cartItem.productImageURL).into(binding.imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val view = RecyclerRowProductCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) =
        holder.bindCartProduct()

    override fun getItemCount(): Int = _cartItems.count()
}