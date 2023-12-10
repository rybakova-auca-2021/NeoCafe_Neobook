package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.R
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory

class PopularProductsAdapter(private var items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_PRODUCT = 1

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ProductCategory -> VIEW_TYPE_HEADER
            is Product -> VIEW_TYPE_PRODUCT
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_PRODUCT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.card_popular_items, parent, false)
                ProductViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val header = items[position] as ProductCategory
                holder.bind(header)
            }
            is ProductViewHolder -> {
                val product = items[position] as Product
                holder.bind(product)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newList: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(
            PopularProductsAdapter.ProductDiffCallback(
                items,
                newList
            )
        )
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }


    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView: TextView = itemView.findViewById(R.id.popular_header)

        fun bind(sectionHeader: ProductCategory) {
            headerTextView.text = sectionHeader.name
        }
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productTitleTextView: TextView = itemView.findViewById(R.id.popular_card_title)
        private val productImageView: ImageView = itemView.findViewById(R.id.popular_card_image)
        private val productPriceView: TextView = itemView.findViewById(R.id.popular_item_price)

        fun bind(product: Product) {
            productTitleTextView.text = product.title
            Glide.with(itemView.context)
                .load(product.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), // Optional: CenterCrop the image if needed
                        RoundedCorners(10) // Set the corner radius in pixels
                    )
                )
                .into(productImageView)
            productPriceView.text = "${product.price} c"

        }
    }

    class ProductDiffCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            (oldList[oldItemPosition] is Product) && (newList[newItemPosition] is Product) &&
                    (oldList[oldItemPosition] as Product).id == (newList[newItemPosition] as Product).id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
