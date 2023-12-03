package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.databinding.CardBranchesBinding
import com.example.neocafe.model.Branch

class BranchesMainAdapter(private var branches: List<Branch>) :
    RecyclerView.Adapter<BranchesMainAdapter.ArticleViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(branch: Branch)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CardBranchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val branch = branches[position]
        holder.bind(branch)
    }

    override fun getItemCount(): Int {
        return branches.size
    }

    fun updateData(newList: List<Branch>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                branches,
                newList
            )
        )
        branches = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ArticleViewHolder(private val binding: CardBranchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = branches[position]
                    itemClickListener?.onItemClick(clickedItem)
                }
            }
        }

        fun bind(branch: Branch) {
//            Glide.with(itemView.context)
//                .load(promotion.image)
//                .apply(
//                    RequestOptions().transform(
//                        CenterCrop(), // Optional: CenterCrop the image if needed
//                        RoundedCorners(10) // Set the corner radius in pixels
//                    )
//                )
//                .into(binding.branchImg)
            binding.branchTitle.text = branch.title
            binding.branchAddress.text = branch.address
            binding.branchTime.text = "${branch.start_time}-${branch.end_time}"
        }
    }

    class ProductDiffCallback(
        private val oldList: List<Branch>,
        private val newList: List<Branch>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].title == newList[newItemPosition].title

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}