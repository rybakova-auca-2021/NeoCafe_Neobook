package com.example.neocafe.view.mainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.AllPromotionsAdapter
import com.example.neocafe.databinding.FragmentAllPromotionsBinding
import com.example.neocafe.model.Promotion
import com.example.neocafe.viewModel.GetPromotionsViewModel

class AllPromotionsFragment : Fragment() {
    private lateinit var binding: FragmentAllPromotionsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllPromotionsAdapter
    private val viewModel: GetPromotionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllPromotionsBinding.inflate(inflater, container, false)
        recyclerView = binding.rvAllPromotions
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AllPromotionsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupNavigation()
        getPromotions()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_allPromotionsFragment_to_mainPageFragment)
        }
        adapter.setOnItemClickListener(object : AllPromotionsAdapter.OnItemClickListener {
            override fun onItemClick(promotion: Promotion) {
                val bundle = Bundle()
                bundle.putInt("id", promotion.id)
                bundle.putString("sourceFragment", "mainPageFragment")
                findNavController().navigate(R.id.detailPromotionFragment, bundle)
            }
        })
    }

    private fun getPromotions() {
        viewModel.getAllPromotions() {
                promotion -> adapter.updateData(promotion)
        }
    }

}