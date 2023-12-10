package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.CategoriesAdapter
import com.example.neocafe.adapters.PopularMainAdapter
import com.example.neocafe.adapters.PopularProductsAdapter
import com.example.neocafe.databinding.FragmentPopularPageBinding
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.viewModel.GetCategoriesViewModel
import com.example.neocafe.viewModel.GetPopularProductsViewModel

class PopularPageFragment : Fragment() {
    private lateinit var binding: FragmentPopularPageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var rv_products: RecyclerView
    private lateinit var productsAdapter: PopularProductsAdapter
    private lateinit var adapter: CategoriesAdapter
    private val viewModel: GetCategoriesViewModel by viewModels()
    private val itemsViewModel: GetPopularProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopularPageBinding.inflate(inflater, container, false)
        recyclerView = binding.rvCategories
        rv_products = binding.rvItems
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupNavigation()
        getCategories()
        getProducts()
        search()
    }

    private fun setupAdapter() {
        adapter = CategoriesAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        productsAdapter = PopularProductsAdapter(emptyList())
        rv_products.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_products.adapter = productsAdapter

        adapter.setOnItemClickListener(object : CategoriesAdapter.OnItemClickListener {
            override fun onItemClick(product: ProductCategory) {
                itemsViewModel.getAllPopularProducts { products ->
                    val items = mutableListOf<Any>()
                    items.add(product)
                    items.addAll(products.filter { it.category == product.id })
                    productsAdapter.updateData(items)
                    binding.rvItems.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.mainPageFragment)
        }
    }

    private fun getCategories() {
        viewModel.getAllCategories { categories ->
            adapter.updateData(categories)
            binding.rvCategories.visibility = View.VISIBLE
        }
    }


    private fun getProducts() {
        itemsViewModel.getAllPopularProducts { products ->
            val items = mutableListOf<Any>()

            viewModel.getAllCategories { categories ->
                val categoryList = categories.toList()

                for (category in categoryList) {
                    items.add(category)
                    items.addAll(products.filter { it.category == category.id })
                }

                productsAdapter.updateData(items)
                binding.rvItems.visibility = View.VISIBLE
            }
        }
    }

    private fun search() {
        binding.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    binding.rvCategories.visibility = View.GONE
                    binding.rvItems.visibility = View.GONE
                    itemsViewModel.getProductsBySearch(newText) { result ->
                        if (result.isEmpty()) {
                            binding.searchMsgResponse.visibility = View.VISIBLE
                            binding.rvItems.visibility = View.GONE
                        } else {
                            binding.searchMsgResponse.visibility = View.GONE
                            binding.rvItems.visibility = View.VISIBLE

                            productsAdapter.updateData(result)
                        }
                    }
                }
                return true
            }
        })
    }
}