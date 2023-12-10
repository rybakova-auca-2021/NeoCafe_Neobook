package com.example.neocafe.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.R
import com.example.neocafe.databinding.CardCategoryBinding
import com.example.neocafe.databinding.CardPopularItemsBinding
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory

class CategoriesAdapter(private var popularItems: List<ProductCategory>) :
    RecyclerView.Adapter<CategoriesAdapter.ArticleViewHolder>() {

    private var selectedCategory: ProductCategory? = null
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(product: ProductCategory)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CardCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val product = popularItems[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return popularItems.size
    }

    fun updateData(newList: List<ProductCategory>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                popularItems,
                newList
            )
        )
        popularItems = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ArticleViewHolder(private val binding: CardCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = popularItems[position]
                    itemClickListener?.onItemClick(clickedItem)
                    selectedCategory = clickedItem
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(product: ProductCategory) {
            binding.categoryName.text = product.name
            Glide.with(itemView.context)
                .load(product.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), // Optional: CenterCrop the image if needed
                        RoundedCorners(10) // Set the corner radius in pixels
                    )
                )
                .into(binding.categoryImg)

            val backgroundColor = if (product == selectedCategory) {
                ContextCompat.getColor(binding.root.context, R.color.main_orange)
            } else {
                Color.TRANSPARENT
            }
            binding.textBackground.setCardBackgroundColor(backgroundColor)
            val textColor = if (product == selectedCategory) {
                ContextCompat.getColor(binding.root.context, R.color.white)
            } else {
                Color.GRAY
            }
            binding.categoryName.setTextColor(textColor)
        }
    }

    class ProductDiffCallback(
        private val oldList: List<ProductCategory>,
        private val newList: List<ProductCategory>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
