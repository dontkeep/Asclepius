package com.dicoding.asclepius.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.local.entity.ResultEntity

class HistoryAdapter(private val onDeleted: (ResultEntity) -> Unit) :
   ListAdapter<ResultEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
   class ViewHolder(private val binding: ItemHistoryBinding) :
      RecyclerView.ViewHolder(binding.root) {
      fun bind(history: ResultEntity, onDeleted: (ResultEntity) -> Unit) {
         binding.date.text = history.date
         binding.pred.text = history.resultText
         binding.inference.text = history.inferenceTime
         Glide.with(itemView.context)
            .load(history.imageUri)
            .into(binding.prevImage)

         binding.btnDelete.setOnClickListener {
            onDeleted(history)
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(view)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val history = getItem(position)
      if (history != null) {
         holder.bind(history, onDeleted)
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultEntity>() {
         override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
            return oldItem == newItem
         }

         override fun areContentsTheSame(
            oldItem: ResultEntity,
            newItem: ResultEntity
         ): Boolean {
            return oldItem == newItem
         }
      }
   }
}