package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.CardPopularItemsBinding
import com.example.neocafe.model.Product

class PopularMainAdapter(private var popularItems: List<Product>) :
    RecyclerView.Adapter<PopularMainAdapter.ArticleViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
        fun onAddClick(product: Product)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CardPopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val product = popularItems[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return popularItems.size
    }

    fun updateData(newList: List<Product>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                popularItems,
                newList
            )
        )
        popularItems = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ArticleViewHolder(private val binding: CardPopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = popularItems[position]
                    itemClickListener?.onItemClick(clickedItem)
                }
            }

            binding.btnAdd.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = popularItems[position]
                    (clickedItem as? Product)?.let { itemClickListener?.onAddClick(it) }
                }
            }
        }

        fun bind(product: Product) {
            binding.popularCardTitle.text = product.title
            Glide.with(itemView.context)
                .load(product.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), // Optional: CenterCrop the image if needed
                        RoundedCorners(10) // Set the corner radius in pixels
                    )
                )
                .into(binding.popularCardImage)
            binding.popularItemPrice.text = "${product.price} c"
        }
    }

    class ProductDiffCallback(
        private val oldList: List<Product>,
        private val newList: List<Product>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
