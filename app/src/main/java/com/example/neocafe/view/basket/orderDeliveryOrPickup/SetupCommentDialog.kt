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

class SetupCommentDialog : DialogFragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_comment, container, false)

        val etComment = view.findViewById<EditText>(R.id.et_comment)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm_comment)
        val btnCancel = view.findViewById<TextView>(R.id.btn_cancel_comment)
        val wordCount = view.findViewById<TextView>(R.id.word_count)

        btnConfirm.isEnabled = etComment.text.isNotEmpty()

        etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val remainingCharacters = 50 - editable?.length!! ?: 0
                wordCount.text = remainingCharacters.toString()
                btnConfirm.isEnabled = remainingCharacters >= 0

                if (remainingCharacters == 0) {
                    wordCount.setTextColor(resources.getColor(R.color.main_orange))
                } else {
                    wordCount.setTextColor(resources.getColor(R.color.grey))
                }
            }
        })

        btnConfirm.setOnClickListener {
            val comments = etComment.text.toString()
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().apply {
                putExtra("comments", comments)
            })
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}
