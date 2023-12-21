package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.R
import com.example.neocafe.adapters.OrdersAdapter2
import com.example.neocafe.databinding.FragmentOrderConfirmedBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.ProductDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderConfirmedFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOrderConfirmedBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter2
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmedBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView2
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getOrders()

        val totalPrice = arguments?.getString("totalPrice")
        val cutleryAmount = arguments?.getInt("cutleryAmount")
        val comment = arguments?.getString("comment")
        val orderNumber = arguments?.getInt("orderNumber", 0)

        binding.orderPrice.text = totalPrice
        binding.orderNumber.text = "Заказ №${orderNumber}"
        binding.clutteryAmount.text = "$cutleryAmount шт"
        binding.orderComment.text = comment
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
            clearBasket()
            findNavController().navigate(R.id.basketFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter2()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    adapter.updateData(orders)
                }
            }
        }
    }

    private fun clearBasket() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()

            if (orders.isNotEmpty()) {
                productDao.deleteProducts(orders)
            }
        }
    }
}