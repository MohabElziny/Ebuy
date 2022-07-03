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
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentAddAddressBinding
import com.iti.android.team1.ebuy.data.data.localsource.LocalSource
import com.iti.android.team1.ebuy.data.data.repository.Repository
import com.iti.android.team1.ebuy.data.pojo.AddressApi
import com.iti.android.team1.ebuy.data.pojo.AddressDto
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddAddressViewModel
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddViewModelFactory
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.AddressResult
import com.iti.android.team1.ebuy.ui.add_address.viewmodel.DataErrorType
import com.iti.android.team1.ebuy.util.getText
import com.iti.android.team1.ebuy.util.showSnackBar
import kotlinx.coroutines.flow.buffer


class AddAddressFragment : Fragment() {

    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddAddressViewModel by viewModels {
        AddViewModelFactory(Repository(LocalSource(requireContext())))
    }

    private val args: AddAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
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
            binding.saveBtn.isClickable = false
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
                setStateResult(it)
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
            binding.saveBtn.isClickable = false
            val address = AddressApi(
                address1 = binding.address1Layout.getText(),
                city = binding.city.getText(),
                province = binding.province.getText(),
                phone = binding.phoneNumber.getText(),
            )
            viewModel.editAddress(args.address.id ?: 0L, AddressDto(address))
        }
        updateData()
    }

    private fun updateData() {
        lifecycleScope.launchWhenStarted {
            viewModel.editAddressState.buffer().collect {
                setStateResult(it)
            }
        }
    }

    private fun setStateResult(result: AddressResult) {
        when (result) {
            is AddressResult.AddAddressError -> {
                showSnackBar(result.errorString)
                binding.saveBtn.isClickable = true
            }
            AddressResult.AddAddressSuccessful -> findNavController().popBackStack()
            is AddressResult.InvalidData -> setInputError(result.error)
            AddressResult.Loading -> resetFields()
        }
    }

    private fun resetFields() {
        binding.apply {
            address1Layout.isErrorEnabled = false
            city.isErrorEnabled = false
            province.isErrorEnabled = false
            phoneNumber.isErrorEnabled = false
        }
    }

    private fun setInputError(error: DataErrorType) {
        binding.saveBtn.isClickable = true
        when (error) {
            DataErrorType.ADDRESS_ERROR -> {
                binding.address1Layout.error = getString(R.string.invalid_address)
            }
            DataErrorType.CITY_ERROR -> {
                binding.city.error = getString(R.string.invalid_city)
            }
            DataErrorType.PHONE_ERROR -> {
                binding.phoneNumber.error = getString(R.string.invalid_phone)
            }
            DataErrorType.PROVINCE_ERROR -> {
                binding.province.error = getString(R.string.invalid_province)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}