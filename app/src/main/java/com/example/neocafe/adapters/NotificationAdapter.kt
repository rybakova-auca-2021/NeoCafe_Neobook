package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.databinding.CardNotificationBinding
import com.example.neocafe.databinding.ItemHeaderBinding
import com.example.neocafe.model.Notification
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(private var notifications: List<Notification>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_DATE_HEADER = 0
    private val VIEW_TYPE_NOTIFICATION = 1

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(notifications: Notification)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 || !areSameDate(notifications[position - 1].created_date, notifications[position].created_date) -> VIEW_TYPE_DATE_HEADER
            else -> VIEW_TYPE_NOTIFICATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_DATE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(inflater, parent, false)
                DateHeaderViewHolder(binding)
            }
            VIEW_TYPE_NOTIFICATION -> {
                val binding = CardNotificationBinding.inflate(inflater, parent, false)
                NotificationViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateHeaderViewHolder -> {
                val header = notifications[position].created_date
                holder.bind(header)
            }
            is NotificationViewHolder -> {
                val notification = notifications[position]
                holder.bind(notification)
            }
        }
    }

    override fun getItemCount(): Int = notifications.size

    fun updateData(newList: List<Notification>) {
        val diffResult = DiffUtil.calculateDiff(
            NotificationDiffCallback(
                notifications,
                newList
            )
        )
        notifications = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private fun areSameDate(date1: String, date2: String): Boolean {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val parsedDate1 = sdf.parse(date1)
        val parsedDate2 = sdf.parse(date2)

        return parsedDate1 != null && parsedDate2 != null && parsedDate1 == parsedDate2
    }

    inner class DateHeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            val formattedHeader = if (isToday(header)) {
                "Сегодня"
            } else {
                header
            }
            binding.popularHeader.text = formattedHeader
        }
    }


    private fun isToday(date: String): Boolean {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val parsedDate = sdf.parse(date)
        return parsedDate != null && sdf.format(currentDate) == sdf.format(parsedDate)
    }

    inner class NotificationViewHolder(private val binding: CardNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = notifications[position]
                    if (clickedItem is Notification) {
                        itemClickListener?.onItemClick(clickedItem)
                    }
                }
            }
        }

        fun bind(notification: Notification) {
            binding.notificationTitle.text = notification.title
            binding.notificationBody.text = notification.body
        }
    }

    class NotificationDiffCallback(
        private val oldList: List<Notification>,
        private val newList: List<Notification>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id && oldList[oldItemPosition].title == newList[newItemPosition].title

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}

