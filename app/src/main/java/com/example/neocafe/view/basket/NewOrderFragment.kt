package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.R
import com.example.neocafe.adapters.CategoriesAdapter
import com.example.neocafe.adapters.OrdersAdapter
import com.example.neocafe.databinding.FragmentMyOrdersBinding
import com.example.neocafe.databinding.FragmentNewOrderBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderFragment : Fragment() {
    private lateinit var binding: FragmentNewOrderBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getOrders()
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                adapter.updateData(orders)
            }
        }
    }

}