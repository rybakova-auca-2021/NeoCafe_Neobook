package com.example.neocafe.view.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentAnswerBinding

class AnswerFragment : Fragment() {
    private lateinit var binding: FragmentAnswerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnswerBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

}