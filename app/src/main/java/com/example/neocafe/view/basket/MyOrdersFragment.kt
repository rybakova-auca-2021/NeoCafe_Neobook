package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.AllPromotionsAdapter
import com.example.neocafe.adapters.MyOrdersAdapter
import com.example.neocafe.databinding.FragmentMyOrdersBinding
import com.example.neocafe.model.GetOrder
import com.example.neocafe.model.Promotion
import com.example.neocafe.viewModel.GetOrdersViewModel

class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    private val viewModel: GetOrdersViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        recyclerView = binding.rvMyOrders
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyOrdersAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupNavigation()
        getPromotions()
    }

    private fun setupNavigation() {
        adapter.setOnItemClickListener(object : MyOrdersAdapter.OnItemClickListener {
            override fun onItemClick(order: GetOrder) {
                val bundle = Bundle()
                bundle.putInt("order_number", order.order_number)
                when (order.status) {
                    "Доставлено" -> {
                        findNavController().navigate(R.id.detailOrderDeliveredFragment, bundle)
                    }
                    else -> {
                        findNavController().navigate(R.id.detailOrderFragment, bundle)
                    }
                }
            }
        })
    }


    private fun getPromotions() {
        viewModel.getMyOrders() {
                orders -> adapter.updateData(orders)
        }
    }
}