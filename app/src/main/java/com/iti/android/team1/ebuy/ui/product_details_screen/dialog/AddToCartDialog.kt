package com.iti.android.team1.ebuy.ui.product_details_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.AddToCartDialougeLayoutBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.AddToCartVMFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.AddToCartViewModel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class AddToCartDialog(private val product: Product) : DialogFragment() {

    private val viewModel: AddToCartViewModel by viewModels {
        AddToCartVMFactory(Repository(LocalSource(requireContext())))
    }

    private var _binding: AddToCartDialougeLayoutBinding? = null
    private val binding get() = _binding!!

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
        viewModel.setProductQuantity(product.productVariants?.get(0)?.productVariantInventoryQuantity
            ?: 0)
        binding.txtProductTitle.text = product.productName
        initViewModelObservers()
        initAddAndCancelButtons()
        initPlusAndMinusButtons()
    }

    private fun initViewModelObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.plusButtonsState.buffer().collect {
                    binding.btnPlus.isEnabled = it
                    if (!it) showMaximumQuantityToast()
                }
            }

            launch {
                viewModel.minusButtonState.buffer().collect {
                    binding.btnMinus.isEnabled = it
                }
            }
            launch {
                viewModel.quantityText.buffer().collect {
                    setTextQuantity(it)
                }
            }
            launch {
                viewModel.addProductClicked.buffer().collect {
                    when (it) {
                        is ResultState.Error -> {
                           enableButtons()
                        }
                        is ResultState.Success -> {
                            dialog?.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun showMaximumQuantityToast() {
        Toast.makeText(requireContext(),
            getString(R.string.no_more_in_stock),
            Toast.LENGTH_SHORT).show()
    }

    private fun setTextQuantity(quantity: Int) {
        binding.textProductQuantity.text = "$quantity"
    }

    private fun initPlusAndMinusButtons() {
        binding.btnPlus.setOnClickListener {
            viewModel.onPressPlusButton()
        }

        binding.btnMinus.setOnClickListener {
            viewModel.onPressMinusButton()
        }
    }

    private fun initAddAndCancelButtons() {
        binding.btnAddToCart.setOnClickListener {
            viewModel.insertProductToCart(product)
            disableButtons()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun disableButtons() {
        binding.btnMinus.isEnabled = false
        binding.btnPlus.isEnabled = false
        binding.btnAddToCart.isEnabled = false
        binding.btnCancel.isEnabled = false
    }

    private fun enableButtons() {
        binding.btnMinus.isEnabled = true
        binding.btnPlus.isEnabled = true
        binding.btnAddToCart.isEnabled = true
        binding.btnCancel.isEnabled = true
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