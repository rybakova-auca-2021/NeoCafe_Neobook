package com.example.neocafe.view.mainPage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentBranchInfoBottomBinding
import com.example.neocafe.model.Branch
import com.example.neocafe.viewModel.GetBranchesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BranchInfoBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBranchInfoBottomBinding
    private val viewModel: GetBranchesViewModel by viewModels()


    companion object {
        private const val ARG_BRANCH_TITLE = "branchTitle"
        private const val ARG_BRANCH_ADDRESS = "branchAddress"

        fun newInstance(branchTitle: String): BranchInfoBottomFragment {
            val args = Bundle()
            args.putString(ARG_BRANCH_TITLE, branchTitle)
            val fragment = BranchInfoBottomFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBranchInfoBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branchTitle = arguments?.getString(ARG_BRANCH_TITLE) ?: ""
        setBranchInfoByTitle(branchTitle)
    }

    fun setBranchInfoByTitle(branchTitle: String) {
        if (isAdded) {
            getBranch(branchTitle)
        }
    }

    private fun getBranch(branchTitle: String) {
        if (isAdded) {
            viewModel.getAllBranches { branches ->
                val specificBranch = branches.firstOrNull { it.title == branchTitle }

                specificBranch?.let {
                    binding.branchName.text = it.title
                    binding.branchAdress.text = it.address
                    binding.branchTime.text = "${it.start_time}-${it.end_time}"
                    binding.branchStatus.text = it.status
                    val address = it.address


                    binding.btnContinue.setOnClickListener {
                        val bundle = Bundle().apply {
                            putString(ARG_BRANCH_ADDRESS, address)
                        }
                        findNavController().navigate(R.id.orderPaymentFragment, bundle)
                        dismiss()
                    }
                } ?: run {
                    // Handle the case where the branch with the specified title is not found
                    // You can show a message or perform other actions here
                }
            }
        }
    }
}

