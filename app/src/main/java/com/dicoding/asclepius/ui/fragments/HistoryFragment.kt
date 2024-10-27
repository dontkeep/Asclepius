package com.dicoding.asclepius.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.ui.adapter.HistoryAdapter
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
   private var _binding: FragmentHistoryBinding? = null
   private val binding get() = _binding!!
   private lateinit var viewModel: HistoryViewModel
   private lateinit var historyAdapter: HistoryAdapter


   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHistoryBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
      viewModel = viewModels<HistoryViewModel> { factory }.value

      setupRecyclerView(view)
      viewModel.allResult.observe(viewLifecycleOwner) {
         historyAdapter.submitList(it)
         if (it.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.emptyHistoryText.visibility = View.VISIBLE
         } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.emptyHistoryText.visibility = View.GONE
         }
      }
   }

   private fun setupRecyclerView(view: View) {
      binding.rvHistory.layoutManager = LinearLayoutManager(view.context)
      binding.rvHistory.adapter = HistoryAdapter(onDeleted = {
         viewModel.deleteResult(it)
         Toast.makeText(view.context, "History deleted", Toast.LENGTH_SHORT).show()
      })
      historyAdapter = binding.rvHistory.adapter as HistoryAdapter
   }
}