package com.iti.android.team1.ebuy.ui.payment_screen.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.android.team1.ebuy.databinding.FragmentPaymentBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModel
import com.iti.android.team1.ebuy.ui.payment_screen.viewmodel.PaymentViewModelFactory
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

private const val TAG = "PaymentFragment"

class PaymentFragment : Fragment() {

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
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    ///TODO: ADD order to API
                    Log.d(TAG, "OnApprove: $captureOrderResult")
                }
            },
            onCancel = OnCancel {
                ///TODO: Indicate the user that he has canceled the order
            },
            onError = OnError { errorInfo ->
                ///TODO: Indicate the user that there's an error the order
                Log.d(TAG, "OnApprove: $errorInfo")

            }

        )
    }

}