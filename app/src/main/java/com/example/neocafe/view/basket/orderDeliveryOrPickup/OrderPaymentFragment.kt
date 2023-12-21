package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentOrderPaymentBinding
import com.example.neocafe.model.Order
import com.example.neocafe.model.OrderItem
import com.example.neocafe.model.ProductItem
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
    private val ARG_BRANCH_ADDRESS = "branchAddress"
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
        if (arguments != null && arguments?.containsKey(ARG_BRANCH_ADDRESS) == true) {
            val address = arguments?.getString(ARG_BRANCH_ADDRESS)
            binding.etBranch.setText(address)
        }

        binding.button.setOnClickListener {
            postOrders()
        }
        setupCards()
        setupBranchPage()
        setupPromocodeDialog()
        setupBonusesDialog()
        setupCommentDialog()
        setupNavigation()
        binding.etPromocode.addTextChangedListener(promocodeTextWatcher)
        binding.etBonuses.addTextChangedListener(bonusesTextWatcher)
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.basketFragment)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setupCards() {
        binding.cardDelivery.setOnClickListener {
            binding.cardDelivery.setCardBackgroundColor(R.color.light_grey)
            binding.deliveryIcon.setImageResource(R.drawable.delivery_icon_orange)
            binding.deliveryPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_orange))
            binding.constraintDelivery.visibility = View.VISIBLE
            binding.constraintPickup.visibility = View.GONE
            binding.cardSelfPickup.setCardBackgroundColor(R.color.white)
            binding.pickupIcon.setImageResource(R.drawable.branch_icon)
            binding.pickupPrice.setTextColor(R.color.grey)
        }

        binding.cardSelfPickup.setOnClickListener {
            binding.cardSelfPickup.setCardBackgroundColor(R.color.light_grey)
            binding.pickupIcon.setImageResource(R.drawable.branch_icon_orange)
            binding.pickupPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_orange))
            binding.constraintPickup.visibility = View.VISIBLE
            binding.constraintDelivery.visibility = View.GONE
            binding.cardDelivery.setCardBackgroundColor(R.color.white)
            binding.deliveryIcon.setImageResource(R.drawable.delivery_icon)
            binding.deliveryPrice.setTextColor(R.color.grey)
        }
    }


    private fun postOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty()) {
                    val orderItems = orders.map { OrderItem(it.id, it.quantity) }
                    val order = Utils.userId?.let {
                        Utils.cutlery?.let { it1 ->
                            Order(
                                products = orderItems,
                                user = it,
                                delivery_type = if (binding.constraintDelivery.visibility == View.VISIBLE) "delivery" else "pickup",
                                address = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etAddress.text.toString() else "",
                                apartment = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etApartment.text.toString() else "",
                                intercom_code = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etCode.text.toString() else "",
                                entrance = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etEntrance.text.toString() else "",
                                floor = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etFloor.text.toString() else "",
                                phone = if (binding.constraintDelivery.visibility == View.VISIBLE) binding.etPhone.text.toString() else "",
                                change_from = try {
                                    binding.etChange.text.toString().toInt()
                                } catch (e: NumberFormatException) {
                                    0
                                },
                                comment = Utils.comment,
                                pickup_branch = if (binding.constraintDelivery.visibility != View.VISIBLE) 1 else 0,
                                cutlery = it1,
                                qr_code =  "",
                                use_bonus = binding.etBonuses.text.toString(),
                                coupon_code = binding.etPromocode.text.toString()
                            )
                        }
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

    private fun getOrdersWithNewPrice() {
        CoroutineScope(Dispatchers.IO).launch {
            val orders = productDao.getAllCartItems()
            val bonusesDialog = binding.etBonuses.text.toString()
            val promocode = binding.etPromocode.text.toString()
            withContext(Dispatchers.Main) {
                if (orders.isNotEmpty() && bonusesDialog.isNotEmpty() && promocode.isNotEmpty()) {
                    binding.totalPrice.visibility = View.GONE
                    binding.cardPrice.visibility = View.VISIBLE
                    val totalPrice = calculateNewTotalPrice(orders)
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

    private fun calculateNewTotalPrice(orders: List<Product>): Double {
        var totalPrice = calculateTotalPrice(orders)
        binding.oldPrice.text = "$totalPrice c"
        val bonuses = binding.etBonuses.text.toString().toDoubleOrNull() ?: 0.0
        totalPrice *= 0.95
        totalPrice -= bonuses
        binding.newPrice.text = "$totalPrice c"
        return totalPrice
    }



    private fun postOrder(order: Order) {
        viewModel.postOrder(order) { postedOrder ->
            val bundle = Bundle()
            bundle.putString("totalPrice", binding.totalPrice.text.toString())
            bundle.putString("comment", postedOrder.comment)
            bundle.putInt("cutleryAmount", postedOrder.cutlery)
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

    private fun setupBranchPage() {
        binding.etBranch.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("requestCode", ADDRESS_REQUEST_CODE)
            findNavController().navigate(R.id.allBranchesFragment, bundle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PROMOCODE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bonusCode = data?.getStringExtra("bonusCode")
                    binding.etPromocode.setText(bonusCode)
                    binding.promoCodeDiscountText.visibility = View.VISIBLE
                }
            }
            BONUSES_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bonuses = data?.getStringExtra("bonuses")
                    binding.etBonuses.setText(bonuses)
                    binding.bonusesDiscountText.text = "(-${bonuses} c)"
                    binding.bonusesDiscountText.visibility = View.VISIBLE
                }
            }
            COMMENTS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val comments = data?.getStringExtra("comments")
                    binding.etComment.setText(comments)
                }
            }
            ADDRESS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val address = data?.getStringExtra("address")
                    println("Address is: $address")
                    binding.etBranch.setText(address)
                }
            }
        }
    }

    private val promocodeTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            getOrdersWithNewPrice()
        }
    }

    private val bonusesTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            getOrdersWithNewPrice()
        }
    }


    companion object {
        const val PROMOCODE_REQUEST_CODE = 1
        const val BONUSES_REQUEST_CODE = 2
        const val COMMENTS_REQUEST_CODE = 3
        const val ADDRESS_REQUEST_CODE = 4
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.etPromocode.removeTextChangedListener(promocodeTextWatcher)
        binding.etBonuses.removeTextChangedListener(bonusesTextWatcher)
    }


}