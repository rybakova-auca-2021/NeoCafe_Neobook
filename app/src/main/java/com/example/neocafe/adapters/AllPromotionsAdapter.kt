package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.CardAllPromotionsBinding
import com.example.neocafe.databinding.CardPromotionsItemsBinding
import com.example.neocafe.model.Promotion

class AllPromotionsAdapter(private var promotions: List<Promotion>) :
    RecyclerView.Adapter<AllPromotionsAdapter.ArticleViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(promotion: Promotion)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CardAllPromotionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val promotion = promotions[position]
        holder.bind(promotion)
    }

    override fun getItemCount(): Int {
        return promotions.size
    }

    fun updateData(newList: List<Promotion>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                promotions,
                newList
            )
        )
        promotions = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ArticleViewHolder(private val binding: CardAllPromotionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = promotions[position]
                    itemClickListener?.onItemClick(clickedItem)
                }
            }
        }

        fun bind(promotion: Promotion) {
            binding.promotionTitle.text = promotion.title
            binding.promotionTitleDesc.text = promotion.promotion_title
            Glide.with(itemView.context)
                .load(promotion.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), // Optional: CenterCrop the image if needed
                        RoundedCorners(10) // Set the corner radius in pixels
                    )
                )
                .into(binding.promotionImg)
        }
    }

    class ProductDiffCallback(
        private val oldList: List<Promotion>,
        private val newList: List<Promotion>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}