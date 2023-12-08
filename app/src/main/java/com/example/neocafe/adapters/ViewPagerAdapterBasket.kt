package com.example.neocafe.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neocafe.view.basket.MyOrdersFragment
import com.example.neocafe.view.basket.NewOrderFragment

class ViewPagerAdapterBasket(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewOrderFragment()
            1 -> MyOrdersFragment()
            else -> Fragment()
        }
    }
}