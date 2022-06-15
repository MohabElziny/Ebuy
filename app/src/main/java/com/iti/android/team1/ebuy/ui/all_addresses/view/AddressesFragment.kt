package com.iti.android.team1.ebuy.ui.all_addresses.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentAddressesBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.ui.all_addresses.adapters.AddressAdapter
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModel
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModelFactory


class AddressesFragment : Fragment() {

    private lateinit var binding: FragmentAddressesBinding
    private lateinit var addressesAdapter: AddressAdapter
    private var destinationID = 0
    private var position: Int? = null
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
                .actionAddressesFragmentToAddAddressFragment(address = Address(),
                    title = getString(R.string.add_address_title)))
        }
        addressesAdapter = AddressAdapter(onItemClick, onDelete, onEdit, onAddSelected,addAddressAsDef)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressesAdapter
        }
        fetchAddresses()
        fetchDeletedData()
        fetchAddressDefChanges()
        viewModel.getAllAddresses()
    }

    private fun fetchAddressDefChanges() {
        viewModel.addressDefState.observe(viewLifecycleOwner) {
            when (it) {
                ResultState.EmptyResult -> {
                    binding.progress.visibility = View.GONE
                    addressesAdapter.changeDefaultAddress(position ?: 0)
                }
                is ResultState.Error -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(requireView(), it.errorString, Snackbar.LENGTH_SHORT).show()
                }
                ResultState.Loading -> binding.progress.visibility = View.VISIBLE
            }
        }
    }

    private fun checkDestination() {
        val args by navArgs<AddressesFragmentArgs>()
        if (args.destination == 1)
            destinationID = 1
    }

    override fun onResume() {
        super.onResume()
        checkDestination()
    }

    private fun fetchAddresses() {
        viewModel.allAddressesState.observe(viewLifecycleOwner) {
            when (it) {
                ResultState.Loading -> setLoadingState()
                is ResultState.Success -> setSuccessState(it)
                ResultState.EmptyResult -> setEmptyState()
                is ResultState.Error -> Snackbar.make(requireView(),
                    it.errorString,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSuccessState(resultState: ResultState.Success<List<Address>>) {
        binding.emptyLayout.root.visibility = View.GONE
        binding.shimmerLayout.root.apply {
            hideShimmer()
            stopShimmer()
            visibility = View.GONE
        }
        binding.floatingActionButton.visibility = View.VISIBLE
        binding.recycler.visibility = View.VISIBLE
        addressesAdapter.setAddresses(resultState.data)
    }

    private fun setLoadingState() {
        binding.emptyLayout.root.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.shimmerLayout.root.apply {
            showShimmer(true)
            startShimmer()
            visibility = View.VISIBLE
        }
    }

    private fun setEmptyState() {
        binding.emptyLayout.root.visibility = View.VISIBLE
        binding.shimmerLayout.root.apply {
            hideShimmer()
            stopShimmer()
            visibility = View.GONE
        }
        binding.recycler.visibility = View.GONE
        binding.floatingActionButton.visibility = View.VISIBLE
        binding.emptyLayout.apply {
            animationView.apply {
                setAnimation(R.raw.no_address)
            }
            txt.text = getString(R.string.no_addresses)
        }
    }

    private fun fetchDeletedData() {
        viewModel.deleteAddressState.observe(viewLifecycleOwner) {
            when (it) {
                ResultState.EmptyResult -> addressesAdapter.deleteItemAtIndex(position ?: 0)
                is ResultState.Error ->
                    Toast.makeText(requireContext(), it.errorString, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onAddSelected: (Address) -> Unit = {
        if (destinationID == 1) {
            // come from check Screen
            val action = AddressesFragmentDirections.actionAddressesFragmentToCartFragment(it,1)
            findNavController().navigate(action)
            destinationID = 0
        }
    }
    private val onItemClick: (Int) -> (Unit) = { }

    private val onDelete: (Address, Int) -> (Unit) = { address, position ->
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Delete Alert")
        dialog.setMessage("Are you sure that you want to delete this address from your account ?")
        dialog.setPositiveButton(android.R.string.ok) { _, _ ->
            this.position = position
            viewModel.deleteAddress(addressId = address.id ?: 0)
        }
        dialog.setNegativeButton(android.R.string.cancel) { _, _ -> }.show()
    }

    private val onEdit: (Address) -> (Unit) = { address ->
        findNavController().navigate(AddressesFragmentDirections
            .actionAddressesFragmentToAddAddressFragment(address,
                title = getString(R.string.edit_address_title)))
    }

    private val addAddressAsDef: (Long, Int) -> (Unit) = { addressId, position ->
        this.position = position
        viewModel.setAddressAsDef(addressId)
    }
}