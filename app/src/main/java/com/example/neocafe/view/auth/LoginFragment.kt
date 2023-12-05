package com.example.neocafe.view.auth

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.neocafe.databinding.FragmentLoginBinding
import com.example.neocafe.view.registration.SendCodeFragment
import com.example.neocafe.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LoginFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupValidation()
        val phone = binding.etPhone.text.toString()
        Utils.phone = phone
    }

    private fun setupNavigation() {
        binding.btnContinue.setOnClickListener {
            login()
        }
        binding.imageView5.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun login() {
        val phone = binding.etPhone.text.toString()
        viewModel.login(phone,
            onSuccess = {
                dismiss()
                val bottomSheetFragment = SendCodeFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            },
            onError = {
                binding.errorMsg.visibility = View.VISIBLE
                binding.etPhone.setTextColor(resources.getColor(R.color.main_orange));
            }
        )
    }

    private fun setupValidation() {
        val textWatchers = arrayOf(
            binding.etPhone
        )
        for (watchedText in textWatchers) {
            watchedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val phone = binding.etPhone.text.toString()
                    binding.btnContinue.isEnabled = phone.isNotEmpty()

                    val whiteColor = resources.getColor(com.example.neocafe.R.color.white)
                    binding.btnContinue.setTextColor(whiteColor)

                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }
}