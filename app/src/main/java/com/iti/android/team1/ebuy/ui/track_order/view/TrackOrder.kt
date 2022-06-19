package com.iti.android.team1.ebuy.ui.track_order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentTrackOrderBinding

class TrackOrder : Fragment() {

    private var _binding: FragmentTrackOrderBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<TrackOrderArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTrackOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOrdersTimeLine()
        bindData()
    }

    private fun bindData() {
        binding.txtOrderNumber.text = args.orderName
        binding.txtFinancialStatus.text = args.orderFinancialStatus
        when (args.orderStatus.lowercase()) {
            "shipping" -> removeShippingOpacity()
            "delivering" -> removeDeliverOpacity()
            "delivered" -> setDeliveredStatus()
            else -> {}
        }
    }

    private fun bindOrdersTimeLine() {
        binding.placedOrder.apply {
            orderIcon.setImageResource(R.drawable.ic_placed)
            orderHeader.text = getText(R.string.order_placed)
            orderSubtitle.text = getText(R.string.we_have_received_your_order)
        }

        binding.shippingOrder.apply {
            root.alpha = 0.5f
            orderIcon.setImageResource(R.drawable.ic_shipped)
            orderHeader.text = getString(R.string.order_shipping)
            orderSubtitle.text = getString(R.string.order_shipping_shubtitle)
        }

        binding.deliveredOrder.apply {
            root.alpha = 0.5f
            orderIcon.setImageResource(R.drawable.ic_delivered)
            orderHeader.text = getString(R.string.order_delivered)
            orderSubtitle.text = getString(R.string.ordere_delivered_subtitle)
        }
    }

    private fun removeShippingOpacity() {
        binding.lineFromPlacingToShipping.setBackgroundResource(R.color.Success)
        binding.shippingOrderStatus.setBackgroundResource(R.drawable.shape_status_current)
        binding.shippingOrder.root.alpha = 1f
    }

    private fun removeDeliverOpacity() {
        removeShippingOpacity()
        binding.shippingOrderStatus.setBackgroundResource(R.drawable.shape_status_completed)
        binding.lineFromShippingToDeliver.setBackgroundResource(R.color.Primary)
    }

    private fun setDeliveredStatus() {
        binding.lineFromShippingToDeliver.setBackgroundResource(R.color.Success)
        binding.lineFromPlacingToShipping.setBackgroundResource(R.color.Success)
        binding.shippingOrderStatus.setBackgroundResource(R.drawable.shape_status_completed)
        binding.deliveredOrderStatus.setBackgroundResource(R.drawable.shape_status_completed)
        binding.shippingOrder.root.alpha = 1f
        binding.deliveredOrder.root.alpha = 1f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}