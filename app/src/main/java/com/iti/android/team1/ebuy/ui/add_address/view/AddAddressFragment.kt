package com.iti.android.team1.ebuy.ui.add_address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentAddAddressBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.model.pojo.AddressApi
import com.iti.android.team1.ebuy.model.pojo.AddressDto
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddAddressViewModel
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddViewModelFactory
import com.iti.android.team1.ebuy.util.getText
import kotlinx.coroutines.flow.buffer


class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding

    private val viewModel: AddAddressViewModel by viewModels {
        AddViewModelFactory(Repository(LocalSource(requireContext())))
    }

    private val args: AddAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSelector()

        if (args.address.address1 != null) {
            setUpdateLayout()
        } else {
            setAddLayout()
        }

    }

    private fun initSelector() {
        val items = resources.getStringArray(R.array.egypt_province_names)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.province.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setAddLayout() {
        binding.saveBtn.setOnClickListener {
            val address = AddressApi(
                address1 = binding.address1Layout.getText(),
                city = binding.city.getText(),
                province = binding.province.getText(),
                phone = binding.phoneNumber.getText(),
            )
            viewModel.addAddress(AddressDto(address))
        }
        fetchState()
    }

    private fun fetchState() {
        lifecycleScope.launchWhenStarted {
            viewModel.addressState.buffer().collect {
                when (it) {
                    ResultState.EmptyResult ->
                        findNavController().popBackStack()

                    is ResultState.Error ->
                        Snackbar.make(requireView(), it.errorString, Snackbar.LENGTH_SHORT).show()

                    ResultState.Loading -> {

                    }
                }
            }
        }
    }

    private fun setUpdateLayout() {
        binding.saveBtn.text = getString(R.string.update_address)
        binding.address1Layout.editText?.setText(args.address.address1)
        binding.city.editText?.setText(args.address.city)
        initSelector()
        binding.phoneNumber.editText?.setText(args.address.phone)
        binding.saveBtn.setOnClickListener {
            updateData()
            val address = AddressApi(
                address1 = binding.address1Layout.getText(),
                city = binding.city.getText(),
                province = binding.province.getText(),
                phone = binding.phoneNumber.getText(),
            )
            viewModel.editAddress(args.address.id ?: 0L, AddressDto(address))
        }

    }

    private fun updateData() {
        lifecycleScope.launchWhenStarted {
            viewModel.editAddressState.buffer().collect {
                when (it) {
                    ResultState.EmptyResult ->
                        findNavController().popBackStack()
                    is ResultState.Error -> {
                        Snackbar.make(requireView(), it.errorString, Snackbar.LENGTH_SHORT).show()
                        binding.saveBtn.isClickable = true
                    }
                    ResultState.Loading -> {
                        binding.saveBtn.isClickable = false
                    }
                }
            }
        }
    }

}