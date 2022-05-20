package com.iti.android.team1.ebuy.credit_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.credit_screen.CreditViewModel
import com.iti.android.team1.ebuy.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    companion object {
        fun newInstance() = CreditFragment()
    }

    private var _binding: FragmentCreditBinding? = null
    private lateinit var viewModel: CreditViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textInputEditExpireNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start + added == 2 && p0?.contains('/') == false) {
                    binding.textInputEditExpireNumber.setText( p0.toString().plus("/"))
                } else if (start == 3 && start - removed == 2 && p0?.contains('/') == true) {
                    binding.textInputEditCardNumber.setText(p0.toString().replace("/", ""))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}