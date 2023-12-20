package com.example.neocafe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.neocafe.databinding.ItemProductImageBinding
import kotlin.math.roundToInt

class ProductImagesAdapter(private val productImages: List<String?>) :
    RecyclerView.Adapter<ProductImagesAdapter.ProductImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding =
            ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        val productImage = productImages[position]
        holder.bind(productImage)
    }

    override fun getItemCount(): Int {
        return productImages.size
    }

    inner class ProductImageViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set layout params for ImageView to enforce a fixed size (50x50 dp)
            val layoutParams = binding.ivProductImage.layoutParams
            layoutParams.width = 40.dpToPx(binding.root.context)
            layoutParams.height = 40.dpToPx(binding.root.context)
            binding.ivProductImage.layoutParams = layoutParams
        }

        fun bind(productImage: String?) {
            // Load product image using Glide or any other image loading library
            Glide.with(binding.root.context)
                .load(productImage)
                .centerCrop()
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), // Optional: CenterCrop the image if needed
                        RoundedCorners(20) // Set the corner radius in pixels
                    )
                ).into(binding.ivProductImage)
        }

        // Extension function to convert dp to pixels
        private fun Int.dpToPx(context: Context): Int {
            val density = context.resources.displayMetrics.density
            return (this * density).roundToInt()
        }
    }
}
