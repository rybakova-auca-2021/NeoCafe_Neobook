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
import com.example.neocafe.adapters.PopularMainAdapter
import com.example.neocafe.databinding.FragmentMainPageBinding
import com.example.neocafe.viewModel.GetProductsViewModel

class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularMainAdapter
    private val viewModel: GetProductsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)
        recyclerView = binding.rvPopular
        (requireActivity() as MainActivity).showBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PopularMainAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        getProducts()
    }

    private fun getProducts() {
        viewModel.getAllProducts() {
                product -> adapter.updateData(product)
        }
    }

}