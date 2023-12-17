package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.CardOrderConfirmBinding
import com.example.neocafe.room.Product

class OrdersAdapter2 : RecyclerView.Adapter<OrdersAdapter2.OrderViewHolder>() {
    private var orders: List<Product> = emptyList()

    fun updateData(newOrders: List<Product>) {
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

    inner class OrderViewHolder(private val binding: CardOrderConfirmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Product) {
            binding.orderTitle.text = order.title
            binding.orderPrice.text = "${order.price} c"
            binding.orderPrice.text = order.price.toString()

            Glide.with(itemView.context)
                .load(order.image)
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
