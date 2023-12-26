package com.example.neocafe.view.mainPage

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.neocafe.MainActivity
import com.example.neocafe.R
import com.example.neocafe.adapters.NotificationsAdapter
import com.example.neocafe.adapters.PopularProductsAdapter
import com.example.neocafe.databinding.FragmentNotificationsBinding
import com.example.neocafe.model.Notification
import com.example.neocafe.model.Product
import com.example.neocafe.viewModel.DeleteNotifications
import com.example.neocafe.viewModel.GetNotificationsViewModel
import com.example.neocafe.viewModel.NotificationViewModel
import com.google.firebase.installations.Utils
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val getNotificationViewModel: GetNotificationsViewModel by viewModels()
    private val deleteNotifications: DeleteNotifications by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        recyclerView = binding.rvNotifications
        swipeRefreshLayout = binding.swipeRefreshLayout

        (requireActivity() as MainActivity).hideBtmNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupNavigation()
        getNotifications()
        setupSwipeRefreshLayout()
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            getNotifications()
            swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun setupAdapter() {
        adapter = NotificationsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            override fun onItemClick(notification: Notification) {
                val bundle = Bundle()
                bundle.putInt("id", notification.id)
                findNavController().navigate(R.id.action_notificationsFragment_to_detailNotificationFragment, bundle)
            }
        })
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.mainPageFragment)
        }
        binding.btnClear.setOnClickListener {
            showDialog()
        }
    }

    private fun getNotifications() {
        getNotificationViewModel.getAllNotifications () {
                notification -> adapter.updateData(notification)
            binding.rvNotifications.visibility = View.VISIBLE
            binding.btnClear.visibility = View.VISIBLE
            binding.noNotificationsMsg.visibility = View.GONE
            binding.imageView25.visibility = View.GONE

            if (notification.isEmpty()) {
                binding.rvNotifications.visibility = View.GONE
                binding.btnClear.visibility = View.GONE
                binding.noNotificationsMsg.visibility = View.VISIBLE
                binding.imageView25.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteNotifications() {
        deleteNotifications.deleteAllNotifications(
            onSuccess = {
                binding.imageView25.visibility = View.VISIBLE
                binding.noNotificationsMsg.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            },
            onError = {
                binding.imageView25.visibility = View.VISIBLE
                binding.noNotificationsMsg.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            }
        )
    }

    private fun showDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_all_notifications, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogView)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val closeBtn = myDialog.findViewById<TextView>(R.id.btn_cancel_delete)
        val logoutBtn = myDialog.findViewById<TextView>(R.id.btn_clear_all_notifications)

        closeBtn.setOnClickListener {
            myDialog.dismiss()
        }
        logoutBtn.setOnClickListener {
            myDialog.dismiss()
            deleteNotifications()
        }

        dialogView.setOnClickListener {
            myDialog.dismiss()
        }
    }
}

