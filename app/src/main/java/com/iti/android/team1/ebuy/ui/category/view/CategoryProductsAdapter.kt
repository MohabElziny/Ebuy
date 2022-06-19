package com.iti.android.team1.ebuy.ui.category.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.android.team1.ebuy.databinding.ProductLayoutBinding
import com.iti.android.team1.ebuy.model.pojo.Product
import com.like.LikeButton
import com.like.OnLikeListener

class CategoryProductsAdapter(
    private val onClickLike: (product: Product) -> Unit,
    private val onClickUnLike: (productID: Long) -> Unit,
    private val onProductClick: (productID: Long) -> Unit,
) :
    RecyclerView.Adapter<CategoryProductsAdapter.ProductHolder>() {

    private var products: List<Product> = emptyList()

    inner class ProductHolder(val binding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val product get() = products[bindingAdapterPosition]

        init {
            binding.likeBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    product.isFavorite = true
                    onClickLike(product)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    product.isFavorite = false
                    product.productID?.let { onClickUnLike(it) }
                }

            })

            binding.parent.setOnClickListener {
                product.productID?.let { it1 -> onProductClick(it1) }
            }
        }

        fun bindData() {
            binding.likeBtn.isLiked = product.isFavorite
            binding.txtProductName.text = product.productName
            binding.txtProductPrice.text =
                product.productVariants?.get(0)?.productVariantPrice.toString().plus(" EGP")
            Glide.with(binding.root.context).load(product.productImage?.imageURL)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding: ProductLayoutBinding =
            ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) = holder.bindData()

    override fun getItemCount(): Int = products.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }


}