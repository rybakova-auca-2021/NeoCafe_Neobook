package com.example.neocafe.view.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.adapters.ViewPagerAdapter
import com.example.neocafe.adapters.ViewPagerAdapterBasket
import com.example.neocafe.databinding.FragmentBasketBinding
import com.google.android.material.tabs.TabLayoutMediator

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.fragmentHolder

        viewPager2.adapter = ViewPagerAdapterBasket(parentFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Новый заказ"
                1 -> tab.text = "Мои заказы"
            }
        }.attach()
        return binding.root
    }
}