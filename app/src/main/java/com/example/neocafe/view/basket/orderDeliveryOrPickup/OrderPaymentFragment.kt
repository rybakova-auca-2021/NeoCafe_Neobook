package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentOrderPaymentBinding
import com.example.neocafe.model.Order
import com.example.neocafe.model.OrderItem
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.viewModel.PostOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderPaymentFragment : Fragment() {
    private lateinit var binding: FragmentOrderPaymentBinding
    private val viewModel: PostOrderViewModel by viewModels()
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPaymentBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrders()
        binding.button.setOnClickListener {
            postOrders()
        }
        setupPromocodeDialog()
        setupBonusesDialog()
        setupCommentDialog()
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.basketFragment)
        }
    }

    private fun postOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    val orderItems = orders.map { OrderItem(it.id, it.quantity) }
                    val order = Utils.userId?.let {
                        Order(
                            products = orderItems,
                            user = it,
                            delivery_type = "delivery",
                            address = binding.etAddress.text.toString(),
                            apartment = binding.etApartment.text.toString(),
                            intercom_code = binding.etCode.text.toString(),
                            entrance = binding.etEntrance.text.toString(),
                            floor = binding.etFloor.text.toString(),
                            phone = binding.etPhone.text.toString(),
                            change_from = try {
                                binding.etChange.text.toString().toInt()
                            } catch (e: NumberFormatException) {
                                0
                            },
                            comment = binding.etComment.text.toString(),
                            pickup_branch = 1,
                            cutlery = 1,
                            qr_code =  "",
                            use_bonus = binding.etBonuses.text.toString(),
                            coupon_code = binding.etPromocode.text.toString()
                        )
                    }
                    if (order != null) {
                        postOrder(order)
                    }
                }
            }
        }
    }

    private fun getOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    val totalPrice = calculateTotalPrice(orders)
                    binding.totalPrice.text = "$totalPrice c"
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

    private fun postOrder(order: Order) {
        viewModel.postOrder(order) { postedOrder ->
            val bundle = Bundle()
            bundle.putString("totalPrice", binding.totalPrice.text.toString())
            bundle.putString("comment", order.comment)
            bundle.putInt("cutleryAmount", order.cutlery)
            bundle.putInt("orderNumber", postedOrder.order_number)

            val bottomSheetFragment = OrderConfirmationFragment()
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun setupPromocodeDialog() {
        binding.etPromocode.setOnClickListener {
            val dialog = SetupPromocodeDialog()
            dialog.setTargetFragment(this, PROMOCODE_REQUEST_CODE)
            dialog.show(parentFragmentManager, "PromocodeDialog")
        }
    }

    private fun setupBonusesDialog() {
        binding.etBonuses.setOnClickListener {
            val dialog = SetupBonusesDialog()
            dialog.setTargetFragment(this, BONUSES_REQUEST_CODE)
            dialog.show(parentFragmentManager, "BonusesDialog")
        }
    }

    private fun setupCommentDialog() {
        binding.etComment.setOnClickListener {
            val dialog = SetupCommentDialog()
            dialog.setTargetFragment(this, COMMENTS_REQUEST_CODE)
            dialog.show(parentFragmentManager, "CommentsDialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PROMOCODE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bonusCode = data?.getStringExtra("bonusCode")
                    binding.etPromocode.setText(bonusCode)
                }
            }
            BONUSES_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bonuses = data?.getStringExtra("bonuses")
                    binding.etBonuses.setText(bonuses)
                }
            }
            COMMENTS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val comments = data?.getStringExtra("comments")
                    binding.etComment.setText(comments)
                }
            }
        }
    }

    companion object {
        const val PROMOCODE_REQUEST_CODE = 1
        const val BONUSES_REQUEST_CODE = 2
        const val COMMENTS_REQUEST_CODE = 3
    }


}