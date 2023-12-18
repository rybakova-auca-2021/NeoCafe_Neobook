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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.neocafe.R

class SetupBonusesDialog : DialogFragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_bonuses, container, false)

        val etBonusCode = view.findViewById<EditText>(R.id.et_bonuses)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm_bonus)
        val btnCancel = view.findViewById<TextView>(R.id.btn_cancel_bonus)

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
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().apply {
                putExtra("bonuses", bonusCode)
            })
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}