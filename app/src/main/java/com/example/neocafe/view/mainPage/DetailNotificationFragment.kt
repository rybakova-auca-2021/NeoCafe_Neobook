package com.example.neocafe.view.mainPage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neocafe.R
import com.example.neocafe.databinding.FragmentDetailNotificationBinding
import com.example.neocafe.viewModel.DeleteNotification
import com.example.neocafe.viewModel.DeleteNotifications
import com.example.neocafe.viewModel.GetNotificationById
import com.example.neocafe.viewModel.NotificationViewModel


class DetailNotificationFragment : Fragment() {
    private lateinit var binding: FragmentDetailNotificationBinding
    private val deleteNotificationViewModel: DeleteNotification by viewModels()
    private val viewModel: GetNotificationById by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            getPromotionDetail(id)
        }
        if (id != null) {
            setupNavigation(id)
        }
    }

    private fun setupNavigation(id: Int) {
        binding.btnBack.setOnClickListener{
           findNavController().navigate(R.id.action_detailNotificationFragment_to_notificationsFragment)
        }
        binding.btnClear.setOnClickListener {
            showDialog(id)
        }
    }

    private fun deleteNotification(id: Int) {
        deleteNotificationViewModel.deleteNotifications(
            id,
            onSuccess = {
                findNavController().navigate(R.id.notificationsFragment)
            },
            onError = {
                findNavController().navigate(R.id.notificationsFragment)
            })
    }

    private fun showDialog(id: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_notification, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogView)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val closeBtn = myDialog.findViewById<TextView>(R.id.btn_cancel_delete_notification)
        val logoutBtn = myDialog.findViewById<TextView>(R.id.btn_clear_notification)

        closeBtn.setOnClickListener {
            myDialog.dismiss()
        }
        logoutBtn.setOnClickListener {
            myDialog.dismiss()
            deleteNotification(id)
        }

        dialogView.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun getPromotionDetail(id: Int) {
        viewModel.getNotification(id,
            onSuccess = {
                    detail ->
                binding.detailTitle.text = detail.title
                binding.detailDesc.text = detail.body
            })
    }
}