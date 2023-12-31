package com.example.neocafe.view.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
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
        setupValidation(userId)
        setupNavigation(userId)
        startCountdownTimer()

    }

    private fun setupNavigation(userId: String) {
        binding.imageView5.setOnClickListener {
            navigateToProfileFragment()
        }
        binding.btnSendCode.setOnClickListener {

        }
    }

    private fun navigateToProfileFragment() {
        findNavController().navigate(R.id.profileFragment)
        dismiss() // Dismiss the fragment after navigating
    }

    private fun checkCode(userId: String, code: String) {
        viewModel.checkCode(code, userId,
            onSuccess = {
                navigateToProfileInfoFragment()
            },
            onError = {
                handleCheckCodeError()
            }
        )
    }

    private fun navigateToProfileInfoFragment() {
        findNavController().navigate(R.id.profileInfoFragment)
        dismiss()
    }

    private fun handleCheckCodeError() {
        binding.pinview.setTextColor(resources.getColor(R.color.main_orange))
        binding.wrongCodeMsg.visibility = View.VISIBLE
    }

    private fun setupValidation(userId: String) {
        val textWatchers = arrayOf(
            binding.pinview
        )

        for (watchedText in textWatchers) {
            watchedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val pin = binding.pinview.text.toString()
                    if (pin.length == 4) {
                        checkCode(userId, pin)
                    }
                }
            })
        }
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                if (isAdded) {
                    binding.time.text = "${millisUntilFinished / 1000} сек"
                }
            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                timeRemaining = 0
                if (isAdded) {
                    binding.btnSendCode.isEnabled = true
                    val whiteColor = resources.getColor(com.example.neocafe.R.color.white)
                    binding.btnSendCode.setTextColor(whiteColor)
                }
            }
        }

        countDownTimer?.start()
    }


    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }

}
