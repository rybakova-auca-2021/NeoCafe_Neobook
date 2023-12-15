package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.databinding.FragmentOrderPaymentBinding


class OrderPaymentFragment : Fragment() {
    private lateinit var binding: FragmentOrderPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }
}