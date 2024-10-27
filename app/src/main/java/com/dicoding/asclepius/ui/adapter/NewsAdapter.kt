package com.dicoding.asclepius.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.network.response.ArticlesItem

class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {
   class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root){
      fun bind(news: ArticlesItem) {
         Glide.with(binding.root.context).load(news.urlToImage).into(binding.itemImage)
         binding.itemTitle.text = news.title
         binding.summaryVertical.text = news.description

         binding.root.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(news.url)
            binding.root.context.startActivity(intent)
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(view)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val getItem = getItem(position)
      holder.bind(getItem)
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
         override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
         }

         override fun areContentsTheSame(
            oldItem: ArticlesItem,
            newItem: ArticlesItem
         ): Boolean {
            return oldItem == newItem
         }
      }
   }
}