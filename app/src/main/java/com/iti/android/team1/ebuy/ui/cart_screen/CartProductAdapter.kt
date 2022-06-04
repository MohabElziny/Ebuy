package com.iti.android.team1.ebuy.ui.cart_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.RecyclerRowProductCartBinding
import com.iti.android.team1.ebuy.model.pojo.CartItem


class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    private var cartProducts: List<CartItem> = emptyList()
    fun setCartProducts(cartProducts: List<CartItem>) {
        this.cartProducts = cartProducts
        notifyDataSetChanged()
    }

    class CartProductViewHolder(var binding: RecyclerRowProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCartProduct(product: CartItem) {
            binding.textProductName.text = product.productName
            binding.textProductSalary.text = product.productVariantPrice.toString().plus("$")
//            binding.textProductInStock.text =
//                if (product.i) "InStock" else "Stock Out"
            binding.textProductQuantity.text = product.variantInventoryQuantity.toString()
            Glide.with(binding.root.context).load(product.productImageURL).into(binding.imageView)
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

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        // to use binding to bind each view with its value
        holder.bindCartProduct(cartProducts[position])
    }

    override fun getItemCount(): Int {
        return cartProducts.count()
    }
}