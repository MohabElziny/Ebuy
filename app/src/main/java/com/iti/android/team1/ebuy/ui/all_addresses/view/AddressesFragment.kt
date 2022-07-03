package com.iti.android.team1.ebuy.ui.all_addresses.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentAddressesBinding
import com.iti.android.team1.ebuy.data.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.data.repository.Repository
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.Address
import com.iti.android.team1.ebuy.data.pojo.ConvertAddToShoppingAdd
import com.iti.android.team1.ebuy.ui.all_addresses.adapters.AddressAdapter
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModel
import com.iti.android.team1.ebuy.ui.all_addresses.viewmodel.AddressesViewModelFactory
import com.iti.android.team1.ebuy.util.showSnackBar

class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding get() = _binding!!
    private var _addressesAdapter: AddressAdapter? = null
    private val addressesAdapter get() = _addressesAdapter!!

    private var destinationID = 0
    private var position: Int? = null
    private val viewModel: AddressesViewModel by viewModels {
        AddressesViewModelFactory(Repository(LocalSource(requireContext())))
    }
    private val args by navArgs<AddressesFragmentArgs>()

    private val deleteBackground = ColorDrawable(Color.RED)
    private lateinit var deleteIcon: Drawable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?,
    ): View {
        _binding = FragmentAddressesBinding.inflate(inflater, container, false)
        _addressesAdapter =
            AddressAdapter(onItemClick, onAddSelected, addAddressAsDef, args.dToCatr)
        deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_24)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(AddressesFragmentDirections
                .actionAddressesFragmentToAddAddressFragment(address = Address(),
                    title = getString(R.string.add_address_title)))
        }
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressesAdapter
        }
        itemTouchHelper.attachToRecyclerView(binding.recycler)
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
                    showSnackBar(it.errorString)
                }
                ResultState.Loading -> binding.progress.visibility = View.VISIBLE
                else -> {}
            }
        }
    }

    private fun checkDestination() {
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
                is ResultState.Error -> showSnackBar(it.errorString)
                else -> {}
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
                ResultState.EmptyResult -> {}
                is ResultState.Error -> {
                    addressesAdapter.restoreDeletedAddress()
                    showSnackBar(it.errorString)
                }
            }
        }
    }

    private val onAddSelected: (Address) -> Unit = { add ->
        if (destinationID == 1) {
            val order = args.order!!
            order.shippingAddress = ConvertAddToShoppingAdd.convertToShipping(add)
            order.billingAddress = ConvertAddToShoppingAdd.convertToBilling(add)
            order.orderStatus = "placed"
            val action = AddressesFragmentDirections.actionAddressesFragmentToPaymentFragment(order)
            findNavController().navigate(action)
            destinationID = 0
        }
    }
    private val onItemClick: (Address) -> (Unit) = { address ->
        findNavController().navigate(AddressesFragmentDirections
            .actionAddressesFragmentToAddAddressFragment(address,
                title = getString(R.string.edit_address_title)))
    }


    private val addAddressAsDef: (Long, Int) -> (Unit) = { addressId, position ->
        this.position = position
        viewModel.setAddressAsDef(addressId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _addressesAdapter = null
        _binding = null
    }

    private var itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder,
            ) = false

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val addressId =
                    addressesAdapter.deleteItemAtIndex(viewHolder.bindingAdapterPosition)
                Snackbar.make(requireView(), "Address deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        addressesAdapter.restoreDeletedAddress()
                    }
                    .addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            //if event == 2 that means the user wants the address to get deleted
                            if (event == 2)
                                viewModel.deleteAddress(addressId)
                        }
                    })
                    .show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {

                viewHolder.itemView.apply {

                    val iconMargin = (height - deleteIcon.intrinsicHeight) / 2

                    if (dX > 0) {
                        deleteBackground.setBounds(left, top, dX.toInt(), bottom)
                        deleteIcon.setBounds(
                            left + iconMargin,
                            top + iconMargin,
                            left + iconMargin + deleteIcon.intrinsicWidth,
                            bottom - iconMargin
                        )
                    } else {
                        deleteBackground.setBounds(right + dX.toInt(), top, right, bottom)
                        deleteIcon.setBounds(
                            right - iconMargin - deleteIcon.intrinsicWidth,
                            top + iconMargin,
                            right - iconMargin,
                            bottom - iconMargin
                        )
                    }

                    deleteBackground.draw(c)
                    c.save()

                    if (dX > 0)
                        c.clipRect(left, top, dX.toInt(), bottom)
                    else
                        c.clipRect(right + dX.toInt(), top, right, bottom)

                    deleteIcon.draw(c)

                    c.restore()
                    super.onChildDraw(c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive)
                }
            }
        })
}