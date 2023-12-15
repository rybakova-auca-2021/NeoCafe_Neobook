package com.example.neocafe.view.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentAnswerBinding
import com.example.neocafe.viewModel.GetPromotionDetail
import com.example.neocafe.viewModel.GetQuestionAnswerViewModel

class AnswerFragment : Fragment() {
    private lateinit var binding: FragmentAnswerBinding
    private val viewModel: GetQuestionAnswerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnswerBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            getQuestionAnswer(id)
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_answerFragment_to_infoFragment)
        }
    }

    private fun getQuestionAnswer(id: Int) {
        viewModel.getQuestionAnswer(id,
            onSuccess = {
                    questionDetail ->
                binding.infoQuestion.text = questionDetail.question
                binding.infoAnswer.text = questionDetail.answer
            }
        )
    }
}