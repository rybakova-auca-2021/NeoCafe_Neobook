package com.example.neocafe.view.basket.orderDeliveryOrPickup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.neocafe.R
import com.example.neocafe.databinding.DialogBonusesBinding
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.Product
import com.example.neocafe.room.ProductDao
import com.example.neocafe.viewModel.BonusesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetupBonusesDialog() : DialogFragment() {
    private val bonusesViewModel: BonusesViewModel by viewModels()
    private lateinit var binding: DialogBonusesBinding
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBonusesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        getBonuses()
    }

    private fun setupViews() {
        binding.apply {
            val etBonusCode = etBonuses
            val btnConfirm = btnConfirmBonus
            val btnCancel = btnCancelBonus

            btnConfirm.isEnabled = etBonusCode.text.isNotEmpty()

            etBonusCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    btnConfirm.isEnabled = !editable.isNullOrEmpty()
                    val whiteColor = resources.getColor(R.color.white)
                    btnConfirm.setTextColor(whiteColor)
                }
            })

            btnCancel.setOnClickListener {
                dismiss()
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

    private fun getBonuses() {
        bonusesViewModel.getBonusesAmount(
            onSuccess = { bonus ->
                binding.bonusesAmount.visibility = View.VISIBLE
                binding.bonusImg.visibility = View.VISIBLE
                binding.bonusesAmount.text = bonus.amount
                binding.btnConfirmBonus.setOnClickListener {
                    val bonusCode = binding.etBonuses.text.toString()

                    if (bonusCode.isEmpty()) {
                        binding.errorMsg.visibility = View.VISIBLE
                    } else {
                        val enteredBonuses = bonusCode.toDouble()

                        if (enteredBonuses > bonus.amount.toDouble()) {
                            binding.errorMsg.visibility = View.VISIBLE
                            binding.bonusesAmount.visibility = View.GONE
                            binding.bonusImg.visibility = View.GONE
                            binding.bonusesAmountText.visibility = View.GONE
                            val orange = resources.getColor(R.color.main_orange)
                            binding.etBonuses.setTextColor(orange)
                        } else {
                            viewLifecycleOwner.lifecycleScope.launch {
                                val orders = productDao.getAllCartItems()
                                if (orders.isNotEmpty()) {
                                    val totalPrice = calculateTotalPrice(orders)

                                    if (enteredBonuses < totalPrice) {
                                        binding.errorMsg.visibility = View.GONE
                                        targetFragment?.onActivityResult(
                                            targetRequestCode,
                                            Activity.RESULT_OK,
                                            Intent().apply {
                                                putExtra("bonuses", bonusCode)
                                            })
                                        dismiss()
                                    } else {
                                        binding.errorMsg2.visibility = View.VISIBLE
                                        binding.bonusesAmount.visibility = View.GONE
                                        binding.bonusImg.visibility = View.GONE
                                        binding.bonusesAmountText.visibility = View.GONE
                                        val orange = resources.getColor(R.color.main_orange)
                                        binding.etBonuses.setTextColor(orange)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
