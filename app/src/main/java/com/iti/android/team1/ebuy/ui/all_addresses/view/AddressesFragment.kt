package com.iti.android.team1.ebuy.ui.all_addresses.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android.team1.ebuy.databinding.FragmentAddressesBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.ui.all_addresses.adapters.AddressAdapter
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModel
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModelFactory
import kotlinx.coroutines.flow.buffer

class AddressesFragment : Fragment() {

    private lateinit var binding: FragmentAddressesBinding
    private lateinit var addressesAdapter: AddressAdapter

    private val viewModel: AddressesViewModel by viewModels {
        AddressesViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?,
    ): View {
        binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(AddressesFragmentDirections
                .actionAddressesFragmentToAddAddressFragment(0))
        }
        fetchAddresses()
        fetchDeletedData()
        viewModel.getAllAddresses()
    }

    private fun fetchDeletedData() {

    }

    private fun fetchAddresses() {
        lifecycleScope.launchWhenStarted {
            viewModel.allAddressesState.buffer().collect {
                when (it) {
                    ResultState.EmptyResult -> {

                    }
                    is ResultState.Error -> {

                    }
                    ResultState.Loading -> {

                    }
                    is ResultState.Success -> {
                        addressesAdapter = AddressAdapter(
                            onItemClick = onItemClick,
                            onDeleteClick = onDelete,
                            onEditClick = onEdit
                        )
                        addressesAdapter.setAddresses(it.data)
                        binding.recycler.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = addressesAdapter
                        }
                    }
                }
            }
        }
    }

    private val onItemClick: (Int) -> (Unit) = { position -> }

    private val onDelete: (Address, Int) -> (Unit) = { address, position ->
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Delete Alert")
        dialog.setMessage("Are you sure that you want to delete this address from your account ?")
        dialog.setPositiveButton(android.R.string.ok) { _, _ ->
            viewModel.deleteAddress(addressId = address.id ?: 0)
            addressesAdapter.deleteItemAtIndex(position)
        }
        dialog.setNegativeButton(android.R.string.cancel) { _, _ ->

        }.show()
    }

    private val onEdit: (Address) -> (Unit) = { address ->
        findNavController().navigate(AddressesFragmentDirections
            .actionAddressesFragmentToAddAddressFragment(address.id ?: 0))
    }
}