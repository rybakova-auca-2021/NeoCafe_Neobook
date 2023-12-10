package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.BranchListAdapter
import com.example.neocafe.adapters.BranchesMainAdapter
import com.example.neocafe.adapters.PopularMainAdapter
import com.example.neocafe.databinding.FragmentListBranchPageBinding
import com.example.neocafe.viewModel.GetBranchesViewModel

class ListBranchPageFragment : Fragment() {
    private lateinit var binding: FragmentListBranchPageBinding
    private val viewModel: GetBranchesViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BranchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBranchPageBinding.inflate(inflater, container, false)
        recyclerView = binding.rvBranches
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getBranches()
    }

    private fun setupAdapter() {
        adapter = BranchListAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun getBranches() {
        viewModel.getAllBranches() {
                branch ->
            adapter.updateData(branch)
        }
    }
}