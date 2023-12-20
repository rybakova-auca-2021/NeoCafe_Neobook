package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.CardOrderConfirmBinding
import com.example.neocafe.model.OrderItem2

class OrderItemAdapter : RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder>() {
    private var orders: List<OrderItem2> = emptyList()

    fun updateData(newOrders: List<OrderItem2>) {
        orders = newOrders
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = CardOrderConfirmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(private val binding: CardOrderConfirmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: OrderItem2) {

            binding.orderTitle.text = productItem.product.title
            binding.orderPrice.text = "${productItem.product.price} c"

            Glide.with(itemView.context)
                .load(productItem.product.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(30)
                    )
                )
                .into(binding.orderImg)
        }
    }
}
