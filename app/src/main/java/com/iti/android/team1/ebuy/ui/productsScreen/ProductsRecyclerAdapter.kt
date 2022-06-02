package com.iti.android.team1.ebuy.ui.productsScreen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.ProductLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products
import com.like.LikeButton
import com.like.OnLikeListener


class ProductsRecyclerAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onLike: (product: Product) -> Unit,
    private val onUnLike: (productId: Long) -> Unit,
) : RecyclerView.Adapter<ProductsRecyclerAdapter.ProductsViewHolder>() {

    private var products: Products = Products(emptyList())
    private lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(newProducts: Products) {
        products = newProducts.copy()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        context = parent.context
        return ProductsViewHolder(ProductLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bindView(products.products!![position])


    override fun getItemCount(): Int = products.products?.size ?: 0

    inner class ProductsViewHolder(private val productLayoutBinding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(productLayoutBinding.root) {

        init {

            productLayoutBinding.parent.setOnClickListener {
                onItemClick(products.products?.get(bindingAdapterPosition)?.productID ?: 0)
            }

            productLayoutBinding.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    products.products?.get(bindingAdapterPosition)?.let { onLike(it) }
                }

                override fun unLiked(likeButton: LikeButton) {
                    products.products?.get(bindingAdapterPosition)?.productID?.let { onUnLike(it) }
                }
            })
        }

        fun bindView(product: Product) {
            Glide.with(context)
                .load(product.productImage!!.imageURL)
                .centerCrop()
                .into(productLayoutBinding.image)
            productLayoutBinding.txtProductName.text = product.productName
            productLayoutBinding.txtProductPrice.text =
                (product.productVariants?.get(0)?.productVariantPrice ?: 0).toString()
            productLayoutBinding.likeBtn.isLiked = product.isFavorite
        }
    }

}