package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.CardItemHeaderBinding
import com.example.neocafe.databinding.CardPopularItemsBinding
import com.example.neocafe.model.Product
import com.example.neocafe.model.ProductCategory
import com.example.neocafe.room.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PopularProductsAdapter(private var items: List<Any>, private val productDao: ProductDao) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_PRODUCT = 1

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_PRODUCT = 1
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
        fun onAddClick(product: Product)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ProductCategory -> VIEW_TYPE_HEADER
            is Product -> VIEW_TYPE_PRODUCT
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = CardItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_PRODUCT -> {
                val binding = CardPopularItemsBinding.inflate(inflater, parent, false)
                ProductViewHolder(binding)
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
                val isProductInRoom = runBlocking {
                    withContext(Dispatchers.IO) {
                        productDao.getProductById(product.id) != null
                    }
                }
                holder.binding.btnAdd.visibility = if (isProductInRoom) View.GONE else View.VISIBLE
                holder.binding.cardAdded.visibility = if (isProductInRoom) View.VISIBLE else View.GONE
                holder.binding.btnPlus.setOnClickListener {
                    updateQuantity(holder, product.quantity + 1, product)
                }
                holder.binding.btnMinus.setOnClickListener {
                    updateQuantity(holder, product.quantity - 1, product)
                }
            }
        }
    }

    private fun updateQuantity(holder: ProductViewHolder, quantity: Int, product: Product) {
        holder.binding.quantityText.text = quantity.toString()
        runBlocking {
            withContext(Dispatchers.IO) {
                productDao.updateProductQuantity(quantity, product.id)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newList: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                items,
                newList
            )
        )
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class HeaderViewHolder(private val binding: CardItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sectionHeader: ProductCategory) {
            binding.itemHeader.text = sectionHeader.name
        }
    }

    inner class ProductViewHolder(val binding: CardPopularItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]
                    (clickedItem as? Product)?.let { itemClickListener?.onItemClick(it) }
                }
            }

            binding.btnAdd.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]
                    (clickedItem as? Product)?.let { itemClickListener?.onAddClick(it) }
                }
                binding.btnAdd.visibility = View.GONE
                binding.cardAdded.visibility = View.VISIBLE
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
