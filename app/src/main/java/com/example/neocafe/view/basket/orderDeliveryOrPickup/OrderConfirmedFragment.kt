package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentOrderConfirmedBinding

class OrderConfirmedFragment : Fragment() {
    private lateinit var binding: FragmentOrderConfirmedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmedBinding.inflate(inflater, container, false)
        return binding.root
    }
}