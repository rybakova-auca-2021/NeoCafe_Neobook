package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.neocafe.MainActivity
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
}