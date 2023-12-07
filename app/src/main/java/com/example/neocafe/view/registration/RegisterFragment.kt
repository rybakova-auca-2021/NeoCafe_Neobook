package com.example.neocafe.view.registration

import RegisterViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.constants.QRCodeUtils
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentRegisterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RegisterFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        binding.imageView5.setOnClickListener {
            findNavController().navigate(com.example.neocafe.R.id.profileFragment)
        }
        binding.btnContinue.setOnClickListener {
            register()
        }
    }

    private fun setupValidation() {
        val textWatchers = arrayOf(
            binding.etName,
            binding.etPhone
        )
        for (watchedText in textWatchers) {
            watchedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val name = binding.etName.text.toString()
                    val phone = binding.etPhone.text.toString()
                    binding.btnContinue.isEnabled = name.isNotEmpty() && phone.isNotEmpty()

                    val whiteColor = resources.getColor(com.example.neocafe.R.color.white)
                    binding.btnContinue.setTextColor(whiteColor)

                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()


        val qrCodeData = "QR Code for registration"
        val qrCodeBitmap = QRCodeUtils.textToImageEncode(qrCodeData, 400)

        println("qrCodeBitmap: $qrCodeBitmap")

        if (qrCodeBitmap != null) {
            val imagePath = QRCodeUtils.saveImage(requireContext(), qrCodeBitmap)
            println("qrCodeImage: $imagePath")
            viewModel.register(requireContext(), name, phone, imagePath,
                onSuccess = {
                    dismiss()
                    val bottomSheetFragment = SendCodeFragment()
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                },
                onError = {
                    binding.phoneErrorMsg.visibility = View.VISIBLE
                    binding.etPhone.setTextColor(resources.getColor(R.color.main_orange));
                }
            )
        }
    }
}