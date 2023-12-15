package com.example.neocafe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafe.databinding.CardInfoLayoutBinding
import com.example.neocafe.model.Question

class AllQuestionsAdapter(private var questions: List<Question>) :
    RecyclerView.Adapter<AllQuestionsAdapter.ArticleViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(question: Question)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = CardInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val promotion = questions[position]
        holder.bind(promotion)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun updateData(newList: List<Question>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                questions,
                newList
            )
        )
        questions = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ArticleViewHolder(private val binding: CardInfoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = questions[position]
                    itemClickListener?.onItemClick(clickedItem)
                }
            }
        }

        fun bind(question: Question) {
            binding.appInfo.text = question.question
        }
    }

    class ProductDiffCallback(
        private val oldList: List<Question>,
        private val newList: List<Question>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}