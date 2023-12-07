package com.example.neocafe.view.mainPage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentQRCodePageBinding
import com.example.neocafe.viewModel.BonusesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.util.Base64

class QRCodePageFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentQRCodePageBinding
    private val bonusesViewModel: BonusesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQRCodePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBonuses()
    }

    private fun getBonuses() {
        bonusesViewModel.getBonusesAmount(
            onSuccess = { bonus ->
                binding.bonusesMsg.text = bonus.amount

                // Load the QR code image using Glide
                Glide.with(this)
                    .load(bonus.qr_code)
                    .into(binding.qrImage)
            }
        )
    }
}