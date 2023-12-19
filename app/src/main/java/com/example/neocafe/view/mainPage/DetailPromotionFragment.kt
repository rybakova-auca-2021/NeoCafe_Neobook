package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDetailPromotionBinding
import com.example.neocafe.viewModel.GetPromotionDetail

class DetailPromotionFragment : Fragment() {
    private lateinit var binding: FragmentDetailPromotionBinding
    private val viewModel: GetPromotionDetail by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPromotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            getPromotionDetail(id)
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            when (arguments?.getString("sourceFragment") ?: "") {
                "orderPaymentBranchFragment" -> findNavController().navigate(R.id.orderPaymentBranchFragment,)
                "orderPaymentFragment" -> findNavController().navigate(R.id.orderPaymentFragment)
                else -> findNavController().popBackStack()
            }
        }
    }

    private fun getPromotionDetail(id: Int) {
        viewModel.getPromotionDetail(id,
            onSuccess = {
                promotionDetail ->
                binding.promotionTitle.text = promotionDetail.promotion_title
                binding.promotionDesc.text = promotionDetail.description
                binding.promotionTime.text = "Акция действует до ${promotionDetail.end_date}"
                Glide.with(binding.promotionImg)
                    .load(promotionDetail.detail_image)
                    .into(binding.promotionImg)
            })
    }
}