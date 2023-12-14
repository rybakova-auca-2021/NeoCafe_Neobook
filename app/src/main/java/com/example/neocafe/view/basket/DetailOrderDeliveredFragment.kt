package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDetailOrderDeliveredBinding

class DetailOrderDeliveredFragment : Fragment() {
    private lateinit var binding: FragmentDetailOrderDeliveredBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailOrderDeliveredBinding.inflate(inflater, container, false)
        return binding.root
    }

}