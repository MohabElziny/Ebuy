package com.iti.android.team1.ebuy.ui.productsScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.android.team1.ebuy.databinding.FragmentProductsBinding
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductViewModelFactory
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductsViewModel

private const val TAG = "ProductsFragment"

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding

    private val viewModel by viewModels<ProductsViewModel> {
        ProductViewModelFactory(
            Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}