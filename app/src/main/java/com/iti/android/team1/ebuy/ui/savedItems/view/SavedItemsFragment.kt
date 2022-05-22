package com.iti.android.team1.ebuy.ui.savedItems.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.SavedItemsLayoutBinding
import com.iti.android.team1.ebuy.ui.savedItems.viewmodel.SavedItemsViewModel

class SavedItemsFragment : Fragment() {

    private lateinit var binding: SavedItemsLayoutBinding
    private lateinit var viewModel: SavedItemsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavedItemsLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

}