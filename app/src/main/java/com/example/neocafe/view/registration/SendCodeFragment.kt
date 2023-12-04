package com.example.neocafe.view.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentSendCodeBinding
import com.example.neocafe.viewModel.CheckCodeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SendCodeFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSendCodeBinding
    private val viewModel: CheckCodeViewModel by viewModels()
    private var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 60000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = Utils.userId.toString()
        val phone = Utils.phone

        binding.codeText.text = "Код был отправлен на номер  $phone"
        setupValidation()
        setupNavigation(userId)
        startCountdownTimer()
    }

    private fun setupNavigation(userId: String) {
        binding.imageView5.setOnClickListener {
            findNavController().navigate(com.example.neocafe.R.id.profileFragment)
        }
        binding.btnSendCode.setOnClickListener {
            checkCode(userId)
        }
    }

    private fun checkCode(userId: String) {
        val code = binding.pinview.text.toString()
        viewModel.checkCode(code, userId,
            onSuccess = {
                dismiss()
                findNavController().navigate(R.id.profileInfoFragment)
            },
            onError = {
                binding.pinview.setTextColor(resources.getColor(R.color.main_orange));
                binding.wrongCodeMsg.visibility = View.VISIBLE
            }
        )
    }

    private fun setupValidation() {
        val textWatchers = arrayOf(
            binding.pinview
        )

        for (watchedText in textWatchers) {
            watchedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val pin = binding.pinview.text.toString()
                    binding.btnSendCode.isEnabled = pin.isNotEmpty()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                binding.time.text = "${millisUntilFinished / 1000} сек"
            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                timeRemaining = 0
                binding.btnSendCode.isEnabled = true
                binding.btnSendCode.setTextColor(resources.getColor(R.color.white));
            }
        }

        countDownTimer?.start()
    }
}