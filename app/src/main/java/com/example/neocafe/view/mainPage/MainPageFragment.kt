package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.BranchesMainAdapter
import com.example.neocafe.adapters.PopularMainAdapter
import com.example.neocafe.adapters.PromotionsMainAdapter
import com.example.neocafe.databinding.FragmentMainPageBinding
import com.example.neocafe.viewModel.GetBranchesViewModel
import com.example.neocafe.viewModel.GetProductsViewModel
import com.example.neocafe.viewModel.GetPromotionsViewModel

class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var promotionRV: RecyclerView
    private lateinit var branchRV: RecyclerView
    private lateinit var adapter: PopularMainAdapter
    private lateinit var promotionAdapter: PromotionsMainAdapter
    private lateinit var branchAdapter: BranchesMainAdapter
    private val viewModel: GetProductsViewModel by viewModels()
    private val promotionViewModel: GetPromotionsViewModel by viewModels()
    private val branchViewModel: GetBranchesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)
        recyclerView = binding.rvPopular
        promotionRV = binding.rvSales
        branchRV = binding.branchesRecyclerView
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupAdapters()
        getProducts()
        getPromotions()
        getBranches()
    }

    private fun setupNavigation() {
        binding.btnAllPromotions.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_allPromotionsFragment)
        }
    }

    private fun setupAdapters() {
        adapter = PopularMainAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        promotionAdapter = PromotionsMainAdapter(emptyList())
        promotionRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        promotionRV.adapter = promotionAdapter

        branchAdapter = BranchesMainAdapter(emptyList())
        branchRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        branchRV.adapter = branchAdapter
    }

    private fun getProducts() {
        viewModel.getAllProducts() {
                product -> adapter.updateData(product)
        }
    }

    private fun getPromotions() {
        promotionViewModel.getAllPromotions() {
                promotion -> promotionAdapter.updateData(promotion)
        }
    }

    private fun getBranches() {
        branchViewModel.getAllBranches() {
                branch -> branchAdapter.updateData(branch)
        }
    }
}