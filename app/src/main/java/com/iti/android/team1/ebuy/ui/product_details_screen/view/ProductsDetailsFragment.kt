package com.iti.android.team1.ebuy.ui.product_details_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentProductsDetailsBinding
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductsDetailsViewModel

class ProductsDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsDetailsFragment()
    }

    private var _binding:FragmentProductsDetailsBinding? = null
    private lateinit var viewModel: ProductsDetailsViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductsDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}