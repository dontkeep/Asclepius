package com.dicoding.asclepius.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.ui.adapter.NewsAdapter
import com.dicoding.asclepius.viewmodel.NewsViewModel

class NewsFragment : Fragment() {
   private var _binding: FragmentNewsBinding? = null
   private val binding get() = _binding!!
   private lateinit var viewModel: NewsViewModel
   private lateinit var newsAdapter: NewsAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentNewsBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
      viewModel = viewModels<NewsViewModel> { factory }.value

      viewModel.getNews()
      setupRecyclerView(view)
      observeData()
      observeLoadingAndError()
   }

   private fun observeLoadingAndError(){
      viewModel.loading.observe(viewLifecycleOwner){
         if (it) {
            binding.rvNews.visibility = View.GONE
            binding.progressbar.visibility = View.VISIBLE
            binding.emptyNewsText.visibility = View.GONE
         } else {
            binding.rvNews.visibility = View.VISIBLE
            binding.progressbar.visibility = View.GONE
         }
      }

      viewModel.errorMessage.observe(viewLifecycleOwner){
         if (it != null) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
         }
      }
   }

   private fun observeData() {
      viewModel.news.observe(viewLifecycleOwner){
         newsAdapter.submitList(it)
         if (it.isEmpty()) {
            binding.rvNews.visibility = View.GONE
            binding.emptyNewsText.visibility = View.VISIBLE
         } else {
            binding.rvNews.visibility = View.VISIBLE
            binding.emptyNewsText.visibility = View.GONE
         }
      }
   }

   private fun setupRecyclerView(view: View) {
      binding.rvNews.layoutManager = LinearLayoutManager(view.context)
      binding.rvNews.adapter = NewsAdapter()
      newsAdapter = binding.rvNews.adapter as NewsAdapter
   }
}