package com.iti.android.team1.ebuy.ui.productsScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iti.android.team1.ebuy.databinding.FragmentProductsBinding
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductsViewModel

class ProductsFragment : Fragment() {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}