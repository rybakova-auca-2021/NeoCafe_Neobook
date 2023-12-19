package com.example.neocafe.view.mainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.BranchesMainAdapter
import com.example.neocafe.adapters.PopularMainAdapter
import com.example.neocafe.adapters.PromotionsMainAdapter
import com.example.neocafe.constants.Utils
import com.example.neocafe.databinding.FragmentMainPageBinding
import com.example.neocafe.model.Branch
import com.example.neocafe.model.Product
import com.example.neocafe.room.MyApplication
import com.example.neocafe.room.ProductDao
import com.example.neocafe.view.basket.OrderBranches.DialogBranchFragment
import com.example.neocafe.view.registration.RegisterFragment
import com.example.neocafe.viewModel.BonusesViewModel
import com.example.neocafe.viewModel.GetBranchesViewModel
import com.example.neocafe.viewModel.GetProductsViewModel
import com.example.neocafe.viewModel.GetPromotionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private val bonusesViewModel: BonusesViewModel by viewModels()
    private val productDao: ProductDao by lazy {
        (requireActivity().application as MyApplication).database.productDao()
    }

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
        getBonuses()
        setupCart()
    }

    private fun setupNavigation() {
        binding.btnAllPromotions.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_allPromotionsFragment)
        }
        binding.btnQrCode.setOnClickListener{
            if (Utils.access == null) {
                findNavController().navigate(R.id.action_mainPageFragment_to_profileFragment)
            } else {
                val bottomSheetFragment = QRCodePageFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
        }
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.profileInfoFragment)
        }
        binding.btnAllBranches.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_allBranchesFragment)
        }
        binding.btnAllPopular.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_popularPageFragment)
        }
        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.notificationsFragment)
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

    private fun setupCart() {
        adapter.setOnItemClickListener(object : PopularMainAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val bundle = Bundle()
                bundle.putInt("id", product.id)
                bundle.putString("sourceFragment", "mainPageFragment")
                findNavController().navigate(R.id.action_mainPageFragment_to_detailProductFragment, bundle)
            }

            override fun onAddClick(product: Product) {
                CoroutineScope(Dispatchers.IO).launch {
                    val cartItem = com.example.neocafe.room.Product(
                        product.id,
                        product.title,
                        product.category,
                        product.image,
                        product.quantity,
                        product.price
                    )
                    productDao.insertCartItem(cartItem)
                }
            }
        })

        branchAdapter.setOnItemClickListener(object : BranchesMainAdapter.OnItemClickListener {
            override fun onItemClick(branch: Branch) {
                val bottomSheetFragment = DialogBranchFragment.newInstance(branch.title)
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
        })
    }

    private fun getProducts() {
        viewModel.getAllProducts() {
                product ->
            val firstFourProducts = product.take(4)
            adapter.updateData(firstFourProducts)
        }
    }

    private fun getPromotions() {
        promotionViewModel.getAllPromotions() {
                promotion -> promotionAdapter.updateData(promotion)
        }
    }

    private fun getBranches() {
        branchViewModel.getAllBranches() {
                branch ->
            val firstFourBranches = branch.take(4)
            branchAdapter.updateData(firstFourBranches)
        }
    }

    private fun getBonuses() {
        bonusesViewModel.getBonusesAmount(
            onSuccess = { bonus ->
                binding.numOfBonuses.text = bonus.amount
            }
        )
    }
}