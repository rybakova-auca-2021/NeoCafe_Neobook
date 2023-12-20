package com.example.neocafe.view.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.OrderItemAdapter
import com.example.neocafe.databinding.FragmentDetailOrderBinding
import com.example.neocafe.viewModel.GetOrderByIdViewModel

class DetailOrderFragment : Fragment() {
    private lateinit var binding: FragmentDetailOrderBinding
    private val viewModel: GetOrderByIdViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailOrderBinding.inflate(inflater, container, false)
        recyclerView = binding.rvOrders
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("order_number")
        if (id != null) {
            getOrder(id)
        }
        setupRecyclerView()
        setupNavigation()
    }
    private fun setupNavigation() {
        binding.imageView8.setOnClickListener {
            findNavController().navigate(R.id.basketFragment)
        }
    }

    private fun getOrder(id: Int) {
        viewModel.getOrder(id,
            onSuccess = {
                    orderDetail ->
                binding.orderNumber.text = "Заказ №${orderDetail.order_number}"
                binding.orderStatus.text = orderDetail.status
                binding.orderPriceRes.text = "${orderDetail.products_amount} c"
                binding.orderBonusesRes.text = "${orderDetail.spent_bonus}"
                binding.orderPromocodeRes.text = "${orderDetail.promo_code}"
                binding.orderClutteryRes.text = "${orderDetail.cutlery} шт"
                binding.orderAddress.text = "${orderDetail.address}"
                binding.orderTotal.text = orderDetail.total_amount
                binding.orderDate.text = orderDetail.created_date
                adapter.updateData(orderDetail.order_item)
            })
    }

    private fun setupRecyclerView() {
        adapter = OrderItemAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}