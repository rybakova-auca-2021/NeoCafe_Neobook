package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentMyOrdersBinding

class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }
}