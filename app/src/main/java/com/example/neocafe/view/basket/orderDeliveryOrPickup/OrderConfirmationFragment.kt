package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentOrderConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class OrderConfirmationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOrderConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }
}