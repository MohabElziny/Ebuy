package com.iti.android.team1.ebuy.ui.payment_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentPaymentBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModel
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModelFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.view.ProductsDetailsFragmentDirections
import com.iti.android.team1.ebuy.util.PAYPAL_CLIENT_ID
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
    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
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
                Toast.makeText(requireContext(),
                    getString(R.string.cancel_order),
                    Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Toast.makeText(requireContext(), errorInfo.reason, Toast.LENGTH_SHORT).show()
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
                    Snackbar.make(binding.root,
                        getString(R.string.order_done),
                        Snackbar.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_paymentFragment_to_navigation_home)

                } else {
                    Snackbar.make(binding.root,
                        getString(R.string.order_error),
                        Snackbar.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    private fun showProgressbar() {
        binding.apply {
            payPalButton.isClickable = false
            payPalButton.isFocusable = false
            payCashButton.isClickable = false
            payCashButton.isFocusable = false
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressbar() {
        binding.apply {
            payPalButton.isClickable = true
            payPalButton.isFocusable = true
            payCashButton.isClickable = true
            payCashButton.isFocusable = true
            progressBar.visibility = View.GONE
        }
    }
}

