package com.example.neocafe.view.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {
    private lateinit var binding: FragmentAppInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppInfoBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupNavigation() {

    }


}