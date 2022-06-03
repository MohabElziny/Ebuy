package com.iti.android.team1.ebuy.ui.product_details_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.iti.android.team1.ebuy.databinding.AddToCartDialougeLayoutBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.AddToCartViewModel
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductDetailsVMFactory

class AddToCartDialog(private val product: Product) : DialogFragment() {

    private val viewModel: AddToCartViewModel by viewModels {
        ProductDetailsVMFactory(Repository(LocalSource(requireContext())))
    }

    private var _binding: AddToCartDialougeLayoutBinding? = null
    private val binding get() = _binding!!
    private var quantity = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = AddToCartDialougeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtProductTitle.text = product.productName
        setTextQuantity()
        initAddAndCancelButtons()
        initPlusAndMinusButtons()
    }

    private fun setTextQuantity() {
        binding.textProductQuantity.text = "$quantity"
    }

    private fun initPlusAndMinusButtons() {
        val productQuantity = product.productVariants?.get(0)?.productVariantInventoryQuantity ?: 0
        binding.btnPlus.setOnClickListener {
            if (quantity < productQuantity) {
                ++quantity
                setTextQuantity()
            } else {
                Toast.makeText(requireContext(),
                    "Only $productQuantity in stock",
                    Toast.LENGTH_SHORT).show()
                binding.btnPlus.isEnabled = false
            }
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                --quantity
                setTextQuantity()
                if (!binding.btnPlus.isEnabled) binding.btnPlus.isEnabled = true
            }
        }
    }

    private fun initAddAndCancelButtons() {
        binding.btnAddToCart.setOnClickListener {
            viewModel.insertProductToCart(product, quantity)
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}