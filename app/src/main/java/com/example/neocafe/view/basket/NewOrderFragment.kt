package com.example.neocafe.view.basket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.OrdersAdapter
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentNewOrderBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.view.basket.orderDeliveryOrPickup.OrderPhoneFragment
import com.example.neocafe.view.basket.orderDeliveryOrPickup.SetupCommentDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderFragment : Fragment() {
    private lateinit var binding: FragmentNewOrderBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private var cutleryQuantity = 0
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getOrders()
        setupNavigation()
        setupCommentDialog()
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cardClutery.visibility = View.VISIBLE
                binding.appliences.visibility = View.GONE
            } else {
                binding.cardClutery.visibility = View.GONE
                binding.appliences.visibility = View.VISIBLE
            }
        }

        binding.btnPlus.setOnClickListener {
            cutleryQuantity++
            binding.orderQuantity.text = cutleryQuantity.toString()
        }

        binding.btnMinus.setOnClickListener {
            if (cutleryQuantity > 0) {
                cutleryQuantity--
                binding.orderQuantity.text = cutleryQuantity.toString()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupNavigation() {
        binding.btnGoToMenu.setOnClickListener{
            findNavController().navigate(R.id.menuFragment)
        }
        binding.btnNext.setOnClickListener {
            if (Utils.access != null) {
                Utils.cutlery = cutleryQuantity
                Utils.comment = binding.commentText.text.toString()
                findNavController().navigate(R.id.orderPaymentFragment)
            } else {
                val bottomSheetFragment = OrderPhoneFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
        }
    }
    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    adapter.updateData(orders)
                    val totalPrice = calculateTotalPrice(orders)
                    binding.basketPrice.text = "$totalPrice c"
                } else {
                    showEmptyCartMessage()
                }
            }
        }
    }

    private fun calculateTotalPrice(orders: List<Product>): Double {
        var totalPrice = 0.00
        for (order in orders) {
            totalPrice += order.price.toDouble()
        }
        return totalPrice
    }

    private fun showEmptyCartMessage() {
        binding.cardOrder.visibility = View.GONE
        binding.emptyBacketImg.visibility = View.VISIBLE
        binding.btnGoToMenu.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun setupCommentDialog() {
        binding.cardComment.setOnClickListener {
            val dialog = SetupCommentDialog()
            dialog.setTargetFragment(this, COMMENTS_REQUEST_CODE)
            dialog.show(parentFragmentManager, "CommentsDialog")
        }
    }

    override fun onResume() {
        super.onResume()
        getOrders()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            COMMENTS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val comments = data?.getStringExtra("comments")
                    binding.commentText.visibility = View.VISIBLE
                    binding.commentText.text = comments
                }
            }
        }
    }

    companion object {
        const val COMMENTS_REQUEST_CODE = 1
    }
}