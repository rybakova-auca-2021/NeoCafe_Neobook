package com.example.neocafe.view.basket

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.neocafe.R
import com.example.neocafe.adapters.ViewPagerAdapter
import com.example.neocafe.adapters.ViewPagerAdapterBasket
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentBasketBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.ProductDao
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrders()
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnClear.setOnClickListener {
            showDialog()
        }
    }

    private fun clearBasket() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()

            if (orders.isNotEmpty()) {
                productDao.deleteProducts(orders)
                withContext(Dispatchers.Main) {
                    binding.btnClear.visibility = View.GONE
                }
            }
        }
    }

    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    binding.btnClear.visibility = View.VISIBLE
                } else {
                    binding.btnClear.visibility = View.GONE
                }
            }
        }
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_basket_clear, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogView)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val closeBtn = myDialog.findViewById<TextView>(R.id.btn_cancel_clear)
        val clearBtn = myDialog.findViewById<TextView>(R.id.btn_clear)

        closeBtn.setOnClickListener {
            myDialog.dismiss()
        }
        clearBtn.setOnClickListener {
            myDialog.dismiss()
            clearBasket()
        }

        dialogView.setOnClickListener {
            myDialog.dismiss()
        }
    }
}