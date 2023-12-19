package com.example.neocafe.view.basket.OrderBranches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.R
import com.example.neocafe.adapters.OrdersAdapter
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentBasket2Binding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.view.basket.orderDeliveryOrPickup.OrderPhoneFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasket2Binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasket2Binding.inflate(inflater, container, false)
        recyclerView = binding.rvOrders
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getOrders()
        setupNavigation()
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupNavigation() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.orderPaymentBranchFragment)
        }
    }
    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    adapter.updateData(orders)
                    val totalPrice = calculateTotalPrice(orders)
                    binding.orderPrice.text = "$totalPrice c"
                }
            }
        }
    }

    private fun calculateTotalPrice(orders: List<Product>): Double {
        var totalPrice = 0.00
        for (order in orders) {
            totalPrice += order.price.toDouble()
        }
        return totalPrice
    }

}