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
import com.example.neocafe.R
import com.example.neocafe.databinding.DialogPromocodeBinding

class SetupPromocodeDialog : DialogFragment() {
    private lateinit var binding: DialogPromocodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPromocodeBinding.inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        binding.apply {
            val etBonusCode = etPromocode
            val btnConfirm = btnConfirmPromocode
            val btnCancel = btnCancelPromocode

            btnConfirm.isEnabled = etBonusCode.text.isNotEmpty()

            etBonusCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    btnConfirm.isEnabled = !editable.isNullOrEmpty()
                    val whiteColor = resources.getColor(com.example.neocafe.R.color.white)
                    btnConfirm.setTextColor(whiteColor)
                }
            })

            btnConfirm.setOnClickListener {
                val bonusCode = etBonusCode.text.toString()
                if (bonusCode == "Chocolate" || bonusCode == "CuteCats") {
                    targetFragment?.onActivityResult(
                        targetRequestCode,
                        Activity.RESULT_OK,
                        Intent().apply {
                            putExtra("bonusCode", bonusCode)
                        })
                    dismiss()
                } else {
                    binding.errorMsg.visibility = View.VISIBLE
                    val orange = resources.getColor(R.color.main_orange)
                    binding.etPromocode.setTextColor(orange)
                }
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}
