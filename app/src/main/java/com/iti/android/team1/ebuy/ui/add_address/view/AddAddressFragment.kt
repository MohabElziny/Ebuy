package com.iti.android.team1.ebuy.ui.add_address.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentAddAddressBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.model.pojo.AddressApi
import com.iti.android.team1.ebuy.model.pojo.AddressDto
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddAddressViewModel
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddViewModelFactory
import com.iti.android.team1.ebuy.util.getText
import kotlinx.coroutines.flow.buffer

private const val TAG = "AddAddressFragment"

class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding

    private val viewModel: AddAddressViewModel by viewModels {
        AddViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = resources.getStringArray(R.array.egypt_province_names)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.province.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.saveBtn.setOnClickListener {
            val address = AddressApi(
                address1 = binding.address1Layout.getText(),
                name = binding.name.getText(),
                city = binding.city.getText(),
                province = binding.province.getText(),
                phone = binding.phoneNumber.getText(),
            )
            Log.d(TAG, "onViewCreated: ${address}")
            viewModel.addAddress(AddressDto(address))
        }
        fetchState()
    }

    private fun fetchState() {
        lifecycleScope.launchWhenStarted {
            viewModel.addressState.buffer().collect {
                when (it) {
                    ResultState.EmptyResult -> {
                        findNavController().popBackStack()
                    }
                    is ResultState.Error -> {
                        Log.d(TAG, "fetchState: ${it.errorString}")
                    }
                    ResultState.Loading -> {
                        Log.d(TAG, "fetchState: loading")
                    }
                    is ResultState.Success -> {
                        Log.d(TAG, "fetchState: Success")
                    }
                }
            }
        }
    }


}