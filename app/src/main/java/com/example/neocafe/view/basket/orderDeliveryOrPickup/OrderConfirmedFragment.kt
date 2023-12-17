package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentOrderConfirmedBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderConfirmedFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOrderConfirmedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val totalPrice = arguments?.getString("totalPrice")
        val cutleryAmount = arguments?.getInt("cutleryAmount", 0)
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
            findNavController().navigate(R.id.myOrdersFragment)
        }
    }
}