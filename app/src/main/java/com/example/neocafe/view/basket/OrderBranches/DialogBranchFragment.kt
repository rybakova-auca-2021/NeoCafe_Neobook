package com.example.neocafe.view.basket.OrderBranches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDialogBranchBinding
import com.example.neocafe.view.mainPage.BranchInfoBottomFragment
import com.example.neocafe.viewModel.GetBranchesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogBranchFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDialogBranchBinding
    private val viewModel: GetBranchesViewModel by viewModels()

    companion object {
        private const val ARG_BRANCH_TITLE = "branchTitle"
        private const val ARG_BRANCH_ADDRESS = "branchAddress"

        fun newInstance(branchTitle: String): DialogBranchFragment {
            val args = Bundle()
            args.putString(ARG_BRANCH_TITLE, branchTitle)
            val fragment = DialogBranchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBranchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val branchTitle = arguments?.getString(DialogBranchFragment.ARG_BRANCH_TITLE) ?: ""
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
                    binding.btnContinue.isEnabled = it.status != "Закрыто"


                    binding.btnContinue.setOnClickListener {
                        findNavController().navigate(R.id.checkoutFragment)
                        dismiss()
                    }
                } ?: run {

                }
            }
        }
    }


}