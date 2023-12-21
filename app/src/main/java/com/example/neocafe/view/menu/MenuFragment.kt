package com.example.neocafe.view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.CategoriesAdapter
import com.example.neocafe.adapters.PopularProductsAdapter
import com.example.neocafe.databinding.FragmentMenuBinding
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.ProductDao
import com.example.neocafe.viewModel.GetCategoriesViewModel
import com.example.neocafe.viewModel.GetProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var rv_products: RecyclerView
    private lateinit var productsAdapter: PopularProductsAdapter
    private lateinit var adapter: CategoriesAdapter
    private val viewModel: GetCategoriesViewModel by viewModels()
    private val itemsViewModel: GetProductsViewModel by viewModels()
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        recyclerView = binding.rvCategories
        rv_products = binding.rvItems
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getCategories()
        getProducts()
        search()
    }

    private fun setupAdapter() {
        adapter = CategoriesAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        productsAdapter = PopularProductsAdapter(emptyList(), productDao)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (productsAdapter.getItemViewType(position)) {
                    PopularProductsAdapter.VIEW_TYPE_HEADER -> 2 // Full span for headers
                    PopularProductsAdapter.VIEW_TYPE_PRODUCT -> 1 // Half span for products
                    else -> 1
                }
            }
        }
        rv_products.layoutManager = layoutManager
        rv_products.adapter = productsAdapter

        adapter.setOnItemClickListener(object : CategoriesAdapter.OnItemClickListener {
            override fun onItemClick(product: ProductCategory) {
                itemsViewModel.getAllProducts { products ->
                    val items = mutableListOf<Any>()
                    items.add(product)
                    items.addAll(products.filter { it.category == product.id })
                    productsAdapter.updateData(items)
                    binding.rvItems.visibility = View.VISIBLE
                }
            }
        })

        productsAdapter.setOnItemClickListener(object : PopularProductsAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val bundle = Bundle()
                bundle.putInt("id", product.id)
                bundle.putString("sourceFragment", "menuPageFragment")
                findNavController().navigate(R.id.action_menuFragment_to_detailProductFragment, bundle)
            }

            override fun onAddClick(product: Product) {
                CoroutineScope(Dispatchers.IO).launch {
                    val cartItem = com.example.neocafe.room.Product(product.id, product.title, product.category, product.image, product.quantity, product.price)
                    productDao.insertCartItem(cartItem)
                }
            }
        })
    }

    private fun getCategories() {
        viewModel.getAllCategories { categories ->
            adapter.updateData(categories)
            binding.rvCategories.visibility = View.VISIBLE
        }
    }


    private fun getProducts() {
        itemsViewModel.getAllProducts { products ->
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