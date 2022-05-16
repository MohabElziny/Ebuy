package com.iti.android.team1.ebuy.cart_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.cart_screen.viewmodel.CartViewModel
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private var _binding: FragmentCartBinding? = null
    private lateinit var viewModel: CartViewModel
    private lateinit var cartProductAdapter: CartProductAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCartRecycler()
    }

    // function
    fun initCartRecycler() {
        cartProductAdapter = CartProductAdapter().apply {
            this.setCartProducts(
                arrayListOf(
                    CartProduct("Mouse", true, 6.0),
                    CartProduct("Mouse", true, 6.0),
                    CartProduct("Mouse", true, 6.0)
                )
            )
        }
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            adapter = cartProductAdapter
        }

    }
}