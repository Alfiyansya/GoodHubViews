package com.alfiansyah.goodhubviews.core.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ReusableAdapter<T : Any>(
    private val context: Context,
    private val layoutResId: Int,
    private val bindViewHolder: (View, T) -> Unit = { _, _ -> },
    private val itemClick: (T) -> Unit = {}
) : RecyclerView.Adapter<ReusableAdapter<T>.ViewHolder>() {
    private var itemList: List<T> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(layoutResId,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun setItems(items:List<T>){
        val diffCallback = DefaultDiffCallback(itemList,items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList = items
        diffResult.dispatchUpdatesTo(this)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList.getOrNull(position)
                    item?.let(itemClick)
                }
            }
        }

        fun bind(item: T) {
            bindViewHolder(itemView, item)
        }
    }

    private class DefaultDiffCallback<T : Any>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}