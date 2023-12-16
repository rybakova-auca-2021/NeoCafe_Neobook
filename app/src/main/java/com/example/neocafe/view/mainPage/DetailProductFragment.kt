package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDetailProductBinding
import com.example.neocafe.databinding.FragmentDetailPromotionBinding
import com.example.neocafe.viewModel.GetProductDetailViewModel
import com.example.neocafe.viewModel.GetPromotionDetail


class DetailProductFragment : Fragment() {
    private lateinit var binding: FragmentDetailProductBinding
    private val viewModel: GetProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            getProductDetail(id)
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            val sourceFragment = arguments?.getString("sourceFragment") ?: ""
            when (sourceFragment) {
                "popularPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_popularPageFragment)
                "mainPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_mainFragment)
                "menuPageFragment" -> findNavController().navigate(R.id.action_detailProductFragment_to_menuFragment)
                else -> findNavController().popBackStack()
            }
        }

    }

    private fun getProductDetail(id: Int) {
        viewModel.getProductDetail(id,
            onSuccess = {
                    productDetail ->
                binding.detailTitle.text = productDetail.title
                binding.detailDesc.text = productDetail.description
                binding.detailPrice.text = productDetail.price
                Glide.with(binding.detailImg)
                    .load(productDetail.image)
                    .into(binding.detailImg)
            })
    }
}