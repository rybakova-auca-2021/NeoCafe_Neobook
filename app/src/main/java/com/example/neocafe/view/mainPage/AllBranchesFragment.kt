package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.R
import com.example.neocafe.adapters.ViewPagerAdapter
import com.example.neocafe.databinding.FragmentAllBranchesBinding
import com.google.android.material.tabs.TabLayoutMediator

class AllBranchesFragment : Fragment() {
    private lateinit var binding: FragmentAllBranchesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBranchesBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.fragmentHolder

        viewPager2.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Карта"
                1 -> tab.text = "Список"
            }
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        // Implement your navigation setup here
    }
}

