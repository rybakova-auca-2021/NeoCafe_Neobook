package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.databinding.CardMyOrdersBinding
import com.example.neocafe.model.GetOrder

class MyOrdersAdapter(private var orders: List<GetOrder>) :
    RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(order: GetOrder)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = CardMyOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun updateData(newList: List<GetOrder>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                orders,
                newList
            )
        )
        orders = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class OrderViewHolder(private val binding: CardMyOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = orders[position]
                    itemClickListener?.onItemClick(clickedItem)
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(order: GetOrder) {
            binding.orderNumber.text = "Заказ №${order.order_number}"
            binding.orderDate.text = order.created_date
            binding.orderTotalPrice.text = "${order.total_amount} c"
        }
    }

    class ProductDiffCallback(
        private val oldList: List<GetOrder>,
        private val newList: List<GetOrder>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].order_number == newList[newItemPosition].order_number

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
