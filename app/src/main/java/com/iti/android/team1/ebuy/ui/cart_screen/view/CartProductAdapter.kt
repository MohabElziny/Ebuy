package com.iti.android.team1.ebuy.ui.cart_screen.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.RecyclerRowProductCartBinding

data class CartProduct(
    var productName: String,
    var inStock: Boolean,
    var productPrice: Double
)

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    private var cartProducts = arrayListOf<CartProduct>()
    fun setCartProducts(cartProducts: ArrayList<CartProduct>) {
        this.cartProducts = cartProducts
        notifyDataSetChanged()
    }

    class CartProductViewHolder(var binding: RecyclerRowProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCartProduct(product: CartProduct) {
            binding.textProductName.text = product.productName
            binding.textProductSalary.text = product.productPrice.toString().plus("$")
            binding.textProductInStock.text =
                if (product.inStock) "InStock" else "Stock Out"
            binding.textProductQuantity.text = "3"
            binding.imageView.setImageResource(R.drawable.a02n)
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