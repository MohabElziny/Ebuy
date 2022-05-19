package com.iti.android.team1.ebuy.ui.category

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val categoryViewModel =
            ViewModelProvider(this).get(CategoryViewModel::class.java)

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        initRecyclerView()



        return binding.root
    }

    fun initRecyclerView() {
        binding.catRecycleView.adapter = CategoryAdapter()
        binding.catRecycleView.layoutManager = GridLayoutManager(context, 2)
        binding.catRecycleView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.category_menu, menu)
        var searchView: SearchView = menu?.findItem(R.id.cat_menu_search)?.actionView as SearchView

        onQueryTextListener(searchView)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun onQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                Toast.makeText(context, "" + p0, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(context, "" + p0, Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }


}