package com.example.neocafe.view.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.R
import com.example.neocafe.adapters.OrdersAdapter
import com.example.neocafe.databinding.FragmentNewOrderBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.view.basket.orderDeliveryOrPickup.OrderPhoneFragment
import com.example.neocafe.constants.Utils
import com.example.neocafe.view.basket.orderDeliveryOrPickup.OrderPaymentFragment
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
        setupNavigation()
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupNavigation() {
        binding.btnGoToMenu.setOnClickListener{
            findNavController().navigate(R.id.menuFragment)
        }
        binding.btnNext.setOnClickListener {
            if (Utils.access != null) {
                findNavController().navigate(R.id.orderPaymentFragment)
            } else {
                val bottomSheetFragment = OrderPhoneFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
        }
    }
    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    adapter.updateData(orders)
                    val totalPrice = calculateTotalPrice(orders)
                    binding.basketPrice.text = "$totalPrice c"
                } else {
                    showEmptyCartMessage()
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

    private fun showEmptyCartMessage() {
        binding.cardOrder.visibility = View.GONE
        binding.emptyBacketImg.visibility = View.VISIBLE
        binding.btnGoToMenu.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        getOrders()
    }
}