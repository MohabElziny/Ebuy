package com.iti.android.team1.ebuy.ui.payment_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentPaymentBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModel
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModelFactory
import com.iti.android.team1.ebuy.util.PAYPAL_CLIENT_ID
import com.iti.android.team1.ebuy.util.showSnackBar
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit

class PaymentFragment : Fragment() {

    private val args by navArgs<PaymentFragmentArgs>()
    private val viewModel: PaymentViewModel by viewModels {
        PaymentViewModelFactory(Repository(LocalSource(requireContext())))
    }
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = CheckoutConfig(
            application = requireActivity().application,
            clientId = PAYPAL_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = "com.iti.android.team1.ebuy://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true,
                shouldFailEligibility = false
            )
        )
        PayPalCheckout.setConfig(config)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.totalPriceTxt.text = args.order.currentTotalPrice + " EGP"
        binding.payPalButton.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD,
                                    value = (args.order.currentTotalPrice!!.toLong() * 0.053).toLong()
                                        .toString())
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture {
                    showProgressbar()
                    viewModel.postOrder(args.order)
                    handlePostResponse()
                }

            },
            onCancel = OnCancel {
                showSnackBar(getString(R.string.cancel_order))
            },
            onError = OnError { errorInfo ->
                showSnackBar(errorInfo.reason)
            }

        )

        binding.payCashButton.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.pay_cash))
            .setMessage("Are you sure you want to place The order?")
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                showProgressbar()
                args.order.financialStatus = "pending"
                viewModel.postOrder(args.order)
                handlePostResponse()
            }
            .show()
    }

    private fun handlePostResponse() {
        lifecycleScope.launchWhenStarted {
            viewModel.requestSucceed.observe(viewLifecycleOwner) { result ->
                hideProgressbar()
                if (result) {
                    showSnackBar(getString(R.string.order_done))
                    findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)

                } else {
                    showSnackBar(getString(R.string.order_error))
                }

            }
        }
    }

    private fun showProgressbar() {
        binding.apply {
            samerfahmy.visibility = View.VISIBLE
            payPalButton.isClickable = false
            payPalButton.isFocusable = false
            payCashButton.isClickable = false
            payCashButton.isFocusable = false
        }
    }

    private fun hideProgressbar() {
        binding.apply {
            samerfahmy.visibility = View.INVISIBLE
            payPalButton.isClickable = true
            payPalButton.isFocusable = true
            payCashButton.isClickable = true
            payCashButton.isFocusable = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

