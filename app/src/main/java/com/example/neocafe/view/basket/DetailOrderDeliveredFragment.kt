package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neocafe.R
import com.example.neocafe.adapters.OrdersAdapter2
import com.example.neocafe.databinding.FragmentDetailOrderBinding
import com.example.neocafe.databinding.FragmentDetailOrderDeliveredBinding
import com.example.neocafe.viewModel.GetOrderByIdViewModel
import com.example.neocafe.viewModel.GetOrdersViewModel

class DetailOrderDeliveredFragment : Fragment() {
    private lateinit var binding: FragmentDetailOrderDeliveredBinding
    private val viewModel: GetOrderByIdViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailOrderDeliveredBinding.inflate(inflater, container, false)
        recyclerView = binding.rvOrders
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("order_number")
        if (id != null) {
            getOrder(id)
        }
        setupRecyclerView()
    }

    private fun getOrder(id: Int) {
        viewModel.getOrder(id,
            onSuccess = {
                    orderDetail ->
                binding.orderNumber.text = "Заказ №${orderDetail.order_number}"
                binding.orderStatus.text = orderDetail.status
                binding.orderPriceRes.text = "${orderDetail.total_amount} c"
                binding.orderAddress.text = "${orderDetail.address}"
                binding.orderTotal.text = orderDetail.total_amount
            })
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter2()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}