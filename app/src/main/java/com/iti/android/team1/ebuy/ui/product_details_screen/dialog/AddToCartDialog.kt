package com.iti.android.team1.ebuy.ui.product_details_screen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.AddToCartDialougeLayoutBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.AddToCartVMFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.AddToCartViewModel
import com.iti.android.team1.ebuy.util.showSnackBar
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
                    if (!it) Snackbar.make(requireActivity().findViewById(R.id.nav_view),
                        getString(R.string.no_more_in_stock),
                        Snackbar.LENGTH_SHORT).show()
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
                        ResultState.EmptyResult -> {}
                        ResultState.Loading -> {}
                    }
                }
            }
        }
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
        binding.btnMinus.isClickable = false
        binding.btnMinus.isFocusable = false
        binding.btnPlus.isClickable = false
        binding.btnPlus.isFocusable = false
        binding.btnAddToCart.isClickable = false
        binding.btnAddToCart.isFocusable = false
        binding.btnCancel.isClickable = false
        binding.btnCancel.isFocusable = false
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun enableButtons() {
        binding.btnMinus.isClickable = true
        binding.btnMinus.isFocusable = true
        binding.btnPlus.isClickable = true
        binding.btnPlus.isFocusable = true
        binding.btnAddToCart.isClickable = true
        binding.btnAddToCart.isFocusable = true
        binding.btnCancel.isClickable = true
        binding.btnCancel.isFocusable = true
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}